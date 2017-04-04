package capstones17_24.psma;

/**
 * Created by Jon on 4/3/2017.
 * Attempts to connect to MATLAB server while Dan is running one -- Works!
 */
import android.app.Activity;
import android.icu.util.Output;
import android.os.AsyncTask;
import android.widget.Toast;

import java.net.*;
import java.io.*;

public class Connection extends AsyncTask {

    @Override
    protected Object doInBackground(Object... arg0) {

        try {
            // explicitly-defined port number
            Integer port = 8000;
            //disp('Connecting to server...')

            // Defining socket location and port
            Socket clientSocket = new Socket("172.27.250.223", port);
            //clientSocket = Socket('localhost', port);

            // I/O stream, currently unused
            InputStream in = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream();
        }

        catch (Exception  e) {

        }
        return null;
    }


}

