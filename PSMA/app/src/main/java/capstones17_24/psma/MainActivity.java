package capstones17_24.psma;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static capstones17_24.psma.Connection.*;

import android.support.v4.app.ActivityCompat;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // no longer using MediaRecorder
    //MediaRecorder myAudioRecorder = new MediaRecorder();

    // 4/12/17, 1pm EDT -- attemtping to get app to record audio...LIKE A BOSS
    // NOW: using AudioRecord built-in Android thing-a-ma-jig
    /*
    NOTE: for the above, this is set up in RecordVoice Activity, will user will
    be taken to when he/she clicks on the button that says to "Record my Cough"
     */

    // this is for connecting to the client
    // - EditText holds IP that app user enters
    // - String holds actual IP address to send when "connect to client" button is tapped/clicked


     /*
    TODO: 4/3/17, 4:37pm EDT || 4/10/17, 4pm EDT || 4/16/17, 8:27pm EDT || 4/17/17, 12:13pm EDT
    TODO: 4/23/17, 6:41pm EDT
    TODO: Tackling addl todo's in this list as well as in Meeting Minutes 4/10/17
        - debug encoding error with array of bytes (pcm should be unchanged and independent from this)
        - tweak Connection code to check BOTH receiving and sending (Dan Request: send as array/matrix)
        - debug and refine (Loop-de-loop)

        (done) change font of title - script is weird, yo
        (done) remove login/create account and directly access feature
        (done) integrate with MATLAB using TCP/IP sockets, Challenge(FTP client)
        (done) prep for presentation (unrelated to app design)
        (done) add activity screen to record the voice (changing to fragment, IF time permits)
        (done) app permissions, re-check (NOTE: enable permissions on phone as well)
        (done) add voice recording capabilities
        (done) save as sound file (.pcm) [but not being used :( ]
        (done) add persistence principles (onPause, onExit, onResume, etc. to sharedPrefs)
        (done) Fix connection error (4/17/2017)
        (done) refine layout/aesthetics and such

    */

    //initialized global var holding BYTE representation of the sound recording
    public static byte[] ByteVersionOfSound = null;

    private EditText clientIP_editText;
    public static String IP_Address;

    public static String StringOfBytes = null;

    // we always need this to start the app given the fact that it is an activity screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);
        // Example of a call to a native method
        //TextView tv = (TextView) findViewById(R.id.sample_text);
        //tv.setText(stringFromJNI());

        // editText field for entering IP Address
        clientIP_editText = (EditText) findViewById(R.id.editTextIP);
        //set listeners
        clientIP_editText.addTextChangedListener(textWatcher);
        // run once to disable if empty
        checkFieldsForEmptyValues();

        SharedPreferences saveIP = getSharedPreferences("IP_address_store", 0);
        clientIP_editText.setText(saveIP.getString("storedIP", IP_Address));

        Button LoginButton = (Button) findViewById(R.id.LoginButton);
        LoginButton.setOnClickListener(this);
        Button clickAbout = (Button) findViewById(R.id.AboutClick);
        clickAbout.setOnClickListener(this);
        Button clickFAQs = (Button) findViewById(R.id.FAQS_Click);
        clickFAQs.setOnClickListener(this);
        Button clickTerms = (Button) findViewById(R.id.TermsClick);
        clickTerms.setOnClickListener(this);
        Button clickHelp = (Button) findViewById(R.id.HelpClick);
        clickHelp.setOnClickListener(this);

        // request recording activity screen
        Button wantRecord = (Button) findViewById(R.id.IwantRecord);
        wantRecord.setOnClickListener(this);

/*      NOTE: Using AudioRecord, and attempting to save as WAV file. See group chat 4/5/17
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile("SampleFile.mp4");
        try {
            myAudioRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    //----------------------------------------------------------------------------------------
    // Functions that are called for related UI interaction between app and user
    //----------------------------------------------------------------------------------------
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.LoginButton: {
                Connection mainFunction = new Connection();
                /*try {
                    mainFunction.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                Toast.makeText(this, "Attempting to connect...", Toast.LENGTH_SHORT).show();
                //Toast.makeText(this, "Recording Cough", Toast.LENGTH_SHORT).show();
                //myAudioRecorder.start();
                Toast.makeText(this, "The byte array is: "+ByteVersionOfSound, Toast.LENGTH_SHORT).show();
                //find_my_byte();

                mainFunction.execute();

                break;
            }
            case R.id.AboutClick: {
                // go to About Us activity
                Toast.makeText(this, "You clicked the 'About Us' button", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.FAQS_Click: {
                // go to FAQs page
                Toast.makeText(this, "You clicked the 'FAQs' button", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.TermsClick: {
                // go to Terms and Conditions page
                Toast.makeText(this, "You clicked the T's & C's button", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.HelpClick: {
                // go to Help Page
                Toast.makeText(this, "You clicked the 'Help' button", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.IwantRecord: {
                // go to the RecordVoice Activity screen
                Toast.makeText(this, "Changing to recording screen...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, RecordVoice.class);
                //EditText editText = (EditText) findViewById(R.id.editText);
                //String message = editText.getText().toString();
                //intent.putExtra(EXTRA_MESSAGE, message);

                startActivity(intent);

                break;
            }
        }

    }

   /*

    public void LoginClick(View v) {
        Toast.makeText(this, "Clicked on the Login Button!", Toast.LENGTH_LONG).show();
    }
*/

    public void CreateAccountClick(View v) {
        Toast.makeText(this, "Clicked on the Account Creation Button!", Toast.LENGTH_LONG).show();
    }

    //TextWatcher
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
        {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            checkFieldsForEmptyValues();
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    private void checkFieldsForEmptyValues(){
        Button b = (Button) findViewById(R.id.LoginButton);

        IP_Address =  clientIP_editText.getText().toString();
        //clientIP_editText.setText("");

        if(IP_Address.equals(""))
        {
            b.setEnabled(false);
        }

        else if(!IP_Address.equals("")){
            b.setEnabled(true);
        }
    }

    /*
    Termination and reopening related code (not closing)
    */

    public void find_my_byte () {
        //SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        //String stringArray = sPrefs.getString("ByteData", null);
        //ByteVersionOfSound = RecordVoice.getBytes(this);

        SharedPreferences sPrefs = getSharedPreferences("ByteData", MODE_PRIVATE);
        String str = sPrefs.getString("arrayOfBytes", null);
        if (str != null) {
            ByteVersionOfSound = str.getBytes(StandardCharsets.UTF_16);
            StringOfBytes = str;

            Toast.makeText(this, "First Byte is: "+ByteVersionOfSound[0], Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Second Byte is: "+ByteVersionOfSound[1], Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Third Byte is: "+ByteVersionOfSound[2], Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Twentieth Byte is: "+ByteVersionOfSound[19], Toast.LENGTH_SHORT).show();
            //Toast.makeText(this, "Size of Byte Array to send: "+ByteVersionOfSound.length, Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    protected void onResume(){
        super.onResume();
        find_my_byte();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // do stuff here
        //find_my_byte();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        // do stuff here
        Toast.makeText(this, "Bruh, why you leavin'?!", Toast.LENGTH_SHORT).show();

        SharedPreferences saveIP = getSharedPreferences("IP_address_store", 0);
        SharedPreferences.Editor edit = saveIP.edit();
        // first run, just add bData to the ByteData-->arrayOfBytes SharedPrefs as a string
        edit.putString("storedIP", IP_Address);
        edit.apply();

    }



//--------------------------------------------------------------------------------------------


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
}
