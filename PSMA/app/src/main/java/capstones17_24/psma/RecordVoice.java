package capstones17_24.psma;

import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

// borrowed from http://stackoverflow.com/questions/8499042/android-audiorecord-example
// will be modified for refinement/tailoring to this Project


public class RecordVoice extends AppCompatActivity {

    private static final int RECORDER_SAMPLERATE = 44100; // for emulator; recomm 44100 for real phone
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private AudioRecord recorder = null;
    private Thread recordingThread = null;
    private boolean isRecording = false;
    private Chronometer myChronometer = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_voice);

        setButtonHandlers();
        enableButtons(false);

        int bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,
                RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);

         myChronometer = (Chronometer)findViewById(R.id.chronometer);
/*
        Button clickStart = (Button) findViewById(R.id.Start);
        clickStart.setOnClickListener((View.OnClickListener) this);
        Button clickStop = (Button) findViewById(R.id.Stahp);
        clickStop.setOnClickListener((View.OnClickListener) this);
*/
    }

    private void setButtonHandlers() {
        ((Button) findViewById(R.id.Start)).setOnClickListener(btnClick);
        ((Button) findViewById(R.id.Stop)).setOnClickListener(btnClick);
    }

    private void enableButton(int id, boolean isEnable) {
        ((Button) findViewById(id)).setEnabled(isEnable);
    }

    private void enableButtons(boolean isRecording) {
        enableButton(R.id.Start, !isRecording);
        enableButton(R.id.Stop, isRecording);
    }

    int BufferElements2Rec = 1024; // want to play 2048 (2K) since 2 bytes we use only 1024
    int BytesPerElement = 2; // 2 bytes in 16bit format

    private void startRecording() {

        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE, RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING, BufferElements2Rec * BytesPerElement);

        recorder.startRecording();
        isRecording = true;
        recordingThread = new Thread(new Runnable() {
            public void run() {
                writeAudioDataToFile();
            }
        }, "AudioRecorder Thread");
        recordingThread.start();
    }

    //convert short to byte
    private byte[] short2byte(short[] sData) {
        int shortArrsize = sData.length;
        byte[] bytes = new byte[shortArrsize * 2];
        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;

    }

    private void writeAudioDataToFile() {
        // Write the output audio in byte
        //String filePath = "/storage/emulated/0/Android/data/com.capstones17_24.psma/SeniorDesign001.pcm";
        String filePath = "/sdcard/SeniorDesign001.pcm";
        short sData[] = new short[BufferElements2Rec];

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (isRecording) {
            // gets the voice output from microphone to byte format

            recorder.read(sData, 0, BufferElements2Rec);
            System.out.println("Short writing to file" + sData.toString());
            try {
                // // writes the data to file from buffer
                // // stores the voice buffer
                byte bData[] = short2byte(sData);
                os.write(bData, 0, BufferElements2Rec * BytesPerElement);

                // Jon Cheng -- 4/17/17, 13:33 EDT
                // while writing bytes to applicable sound file, I'm storing raw byte in sharedPrefs
                // SharedPrefs allows data to persist permanently in the app, and is retrievable
                // throughout the activity screens when explicitly called by the name it was saved under...bruh
                SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(RecordVoice.this);

                SharedPreferences.Editor sEdit = sPrefs.edit();
                sEdit.putString("ByteData", Arrays.toString(bData));
                //sEdit.putInt("size",bData.length);
                sEdit.commit();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        // stops the recording activity
        if (null != recorder) {
            isRecording = false;
            recorder.stop();
            recorder.release();
            recorder = null;
            recordingThread = null;
        }
    }

    private View.OnClickListener btnClick = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.Start: {
                    enableButtons(true);
                    startRecording();
                    myChronometer.setBase(SystemClock.elapsedRealtime());
                    myChronometer.start();
                    break;
                }
                case R.id.Stop: {
                    enableButtons(false);
                    stopRecording();
                    myChronometer.stop();
                    break;
                }
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
