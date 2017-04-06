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

import static capstones17_24.psma.MainActivity.IP_Address;

public class Connection extends AsyncTask {

    @Override
    protected Object doInBackground(Object... arg0) {

        try {
            // explicitly-defined port number
            Integer port = 8000;
            //disp('Connecting to server...')

            // Defining socket location and port -- IP Changes bc RUWireless dynamic
            Socket clientSocket = new Socket(IP_Address, port);
            //clientSocket = Socket('localhost', port);

            /*PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            pw.write("This is Capstone Group S24 (Ft. DJ Jay-C)");
            pw.flush();
            pw.close();*/

            //--------------------------------------------------------------
            // preparing to send message...
            DataOutputStream dataOut = new DataOutputStream(clientSocket.getOutputStream());

            // message receives and prints, but some extra character/squareish thing appears in front (Square, space and another Square)
            //dataOut.writeByte(1); // this doesn't do anything for us! D:
            //dataOut.writeUTF("This is Capstone Group S24 (Ft. DJ Jay-C)");
            //dataOut.writeUTF("Hello from Capstone S24!");
            dataOut.writeBytes("Hello from Capstone S17-24!");
            dataOut.flush();

            //closing stream
            dataOut.close();
            //--------------------------------------------------------------

            // checks if connection is closed or not
            if (!clientSocket.isClosed()) {
                clientSocket.close();
            }

            // I/O stream, currently unused
            //InputStream in = clientSocket.getInputStream();
            //OutputStream outStream = clientSocket.getOutputStream();
        }

        catch (Exception  e) {

        }
        return null;
    }


}

/*
Resources
http://stackoverflow.com/questions/5680259/using-sockets-to-send-and-receive-data
 */
