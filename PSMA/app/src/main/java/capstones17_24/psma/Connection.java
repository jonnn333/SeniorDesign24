package capstones17_24.psma;

/**
 * Created by Jon on 4/3/2017.
 * Attempts to connect to
 */
import android.app.Activity;
import android.icu.util.Output;

import java.net.*;
import java.io.*;

public class Connection extends Activity {

    public static void main() throws IOException {

        Integer port = 8000; // explicitly-defined port number
        //disp('Connecting to server...')

        Socket clientSocket = new Socket("172.27.250.223", port);
        //clientSocket = Socket('localhost', port);

        InputStream in = clientSocket.getInputStream();
        OutputStream out = clientSocket.getOutputStream();
    }


}
