package capstones17_24.psma;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.Random;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static capstones17_24.psma.Connection.*;

import android.support.v4.app.ActivityCompat;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);
        // Example of a call to a native method
        //TextView tv = (TextView) findViewById(R.id.sample_text);
        //tv.setText(stringFromJNI());

        Button clickButton = (Button) findViewById(R.id.LoginButton);
        clickButton.setOnClickListener(this);
        Button clickAbout = (Button) findViewById(R.id.AboutClick);
        clickAbout.setOnClickListener(this);
        Button clickFAQs = (Button) findViewById(R.id.FAQS_Click);
        clickFAQs.setOnClickListener(this);
        Button clickTerms = (Button) findViewById(R.id.TermsClick);
        clickTerms.setOnClickListener(this);
        Button clickHelp = (Button) findViewById(R.id.HelpClick);
        clickHelp.setOnClickListener(this);

    }

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
                Toast.makeText(this, "You clicked the 'Terms and Conditions' button", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.HelpClick: {
                // go to Help Page
                Toast.makeText(this, "You clicked the 'Help' button", Toast.LENGTH_SHORT).show();
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
