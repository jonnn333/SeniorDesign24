package capstones17_24.psma;

import android.content.Context;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.*;
import java.util.List;


public class RecordVoice extends AppCompatActivity {

    private static final int RECORDER_SAMPLERATE = 44100; // 8000 for emulator; recomm 44100 for real phone

    // means that it just uses a single channel for audio signal
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;

    // Audio data format: PCM 16 bit per sample. Guaranteed to be supported by devices.
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    // initializing AudioRecord class, a thread for it to run on, boolean to check if it is recording
    private AudioRecord recorder = null;
    private Thread recordingThread = null;
    private boolean isRecording = false;

    // initializing TIMER
    private Chronometer myChronometer = null;

    private byte[] oldToNewByteArray = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_voice);

        setButtonHandlers();
        enableButtons(false);

        int bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,
                RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);

         myChronometer = (Chronometer)findViewById(R.id.chronometer);
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

    // Google up on this information... 4/23
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

                // Jon Cheng -- 4/17/17, 1:33 PM  EDT
                // Edit 4/19/17, 1:40 AM EDT (sharedPrefs more or less implemented, testing now)
                // Edit 4/19/17, 8 PM EDT (shit still doesn't work, bruh) Re-designing sharedPrefs implem'n
                //      Addendum to 4/19: I've been saving the bytes that are stored while this fcn is being called,
                //      meaning that I'm saving ONLY whatever's passing through, then overwriting another tiny snippet...
                //      I need to find a way to safely and correctly pass Bytes to sharedPrefs, loading them back and converting,
                //      and then adding onto the same array, all while placing it back into sharedPrefs

                // while writing bytes to applicable sound file, I'm storing raw byte in sharedPrefs
                // SharedPrefs allows data to persist permanently in the app, and is retrievable
                // throughout the activity screens when explicitly called by the name it was saved under...bruh
                //SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(RecordVoice.this);
                //SharedPreferences.Editor sEdit = sPrefs.edit();

                SharedPreferences sPrefs = getSharedPreferences("ByteData", MODE_PRIVATE);

                //if sharedPreferences contains the save site called "ByteData", edit and add onto it
                if (sPrefs.contains("ByteData")) {

                    SharedPreferences.Editor edit = sPrefs.edit();
                    byte[] oldBytes = null;

                    // open up arrayOfBytes and get the String-->bytes format
                    String tempString = sPrefs.getString("arrayOfBytes", null);
                    if (tempString != null ) {
                        oldBytes = tempString.getBytes(StandardCharsets.UTF_16);
                    }

                    // format to combine 2 arrays using arraycopy
                    //byte[] combined = new byte[one.length + two.length];
                    //System.arraycopy(one,0,combined,0         ,one.length);
                    //System.arraycopy(two,0,combined,one.length,two.length);

                    /*
                    public static void arraycopy(Object src, int srcPos, Object dest, int destPos, int length)
                    src − This is the source array.
                    srcPos − This is the starting position in the source array.
                    dest − This is the destination array.
                    destPos − This is the starting position in the destination data.
                    length − This is the number of array elements to be copied.
                     */

                    byte[] combined = new byte[oldBytes.length + bData.length];
                    System.arraycopy(oldBytes, 0, combined, 0, oldBytes.length);
                    System.arraycopy(bData, 0, combined, oldBytes.length, bData.length);

                    Toast.makeText(this, "Element 3 of combined array is: "+combined[2], Toast.LENGTH_SHORT).show();
                    edit.putString("arrayOfBytes", new String(combined, StandardCharsets.UTF_16));

                    // save changes
                    edit.apply();
                }

                // else, write to it for the first time ev'uh
                else {
                    SharedPreferences.Editor edit = sPrefs.edit();
                    // first run, just add bData to the ByteData-->arrayOfBytes SharedPrefs as a string
                    edit.putString("arrayOfBytes", new String(bData, StandardCharsets.UTF_16));

                    // save changes
                    edit.apply();
                }


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


    public void clear(Context context) {
        SharedPreferences prefs = getSharedPreferences("ByteData", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 4/19/17 - 8:53pm EDT
        // don't do this at home, kids.
        // rm -rf (JK, but really don't do this on a Linux terminal for any reason whatsoever. furreals)

        clear(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //clear(this);
    }
}
