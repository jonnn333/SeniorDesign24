package capstones17_24.psma;

/*
 * Created by Jon on 4/3/2017.
 * Attempts to connect to MATLAB server while Dan is running one -- Works!
 */
import android.app.Activity;
import android.icu.util.Output;
import android.os.AsyncTask;
import android.widget.Toast;

import java.net.*;
import java.io.*;

import static capstones17_24.psma.MainActivity.ByteVersionOfSound;
import static capstones17_24.psma.MainActivity.IP_Address;


public class Connection extends AsyncTask {

    private static Socket clientSocket;
    private OutputStream out;

    @Override
    protected Object doInBackground(Object... arg0) {

        try {
            // explicitly-defined port number
            Integer port = 8000;
            //disp('Connecting to server...')

            // Defining socket location and port -- IP Changes bc RUWireless dynamic
            clientSocket = new Socket(IP_Address, port);
            //clientSocket = Socket('localhost', port);

            /*PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            pw.write("This is Capstone Group S24 (Ft. DJ Jay-C)");
            pw.flush();
            pw.close();*/

            //--------------------------------------------------------------
            // preparing to send message...
            //DataOutputStream dataOut = new DataOutputStream(clientSocket.getOutputStream());

            // message receives and prints, but some extra character/squareish thing appears in front (Square, space and another Square)
            //dataOut.writeByte(1); // this doesn't do anything for us! D:
            //dataOut.writeUTF("This is Capstone Group S24 (Ft. DJ Jay-C)");
            //dataOut.writeUTF("Hello from Capstone S24!");

            // 4/17/17 -- WANT: to send byte array of the sound recording to MATLAB Server
            // 4/17/17 - snippet below was not commented out when the whole chunk up^^ there were run
            sendBytes(ByteVersionOfSound);

            // OLD:
            //dataOut.writeBytes("Hello from Capstone S17-24!");
            //dataOut.flush();

            //closing stream
            //dataOut.close();
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
            e.printStackTrace();
        }
        return null;
    }

    private void sendBytes(byte[] myByteArray) throws IOException {
        sendBytes(myByteArray, 0, myByteArray.length);
    }


    private void sendBytes(byte[] myByteArray, int start, int len) throws IOException {
        if (len < 0)
            throw new IllegalArgumentException("Negative length not allowed");
        if (start < 0 || start >= myByteArray.length)
            throw new IndexOutOfBoundsException("Out of bounds: " + start);
        // Other checks if needed.

        // May be better to save the streams in the support class;
        // just like the socket variable.
        out = clientSocket.getOutputStream();
        DataOutputStream dos = new DataOutputStream(out);

        if (len > 0) {
            //dos.write(myByteArray, start, len);
            dos.writeUTF(MainActivity.StringOfBytes);
        }
        dos.flush();
        dos.close();
    }


}

/*
Resources
http://stackoverflow.com/questions/5680259/using-sockets-to-send-and-receive-data
http://stackoverflow.com/questions/2878867/how-to-send-an-array-of-bytes-over-a-tcp-connection-java-programming

http://stackoverflow.com/questions/28150362/what-is-the-difference-between-byte-and-listbyte-in-java

borrowed from http://stackoverflow.com/questions/8499042/android-audiorecord-example
will be modified for refinement/tailoring to this Project

http://stackoverflow.com/questions/20682865/disable-button-when-edit-text-fields-empty
 */
