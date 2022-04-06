package distributedSystems;

import java.io.*;
import java.net.*;

public class Client extends Thread {
    int a, b;
    Client(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public void run() {

        /* Create socket for contacting the server on port 4321*/
        Socket requestSocket = null;

        /* Create the streams to send and receive data from server */
        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        try {
            requestSocket = new Socket("127.0.0.1", 4321);
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            /* Write the two integers */
            out.writeInt(a);
            out.flush();

            out.writeInt(b);
            out.flush();




            /* Print the received result from server */
            System.out.println("Server>" + in.readInt());

        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            try {
                in.close(); out.close();
                requestSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public static void main(String args[]) {
        new Client(10, 5).start();
        new Client(20, 5).start();
        new Client(30, 5).start();
        new Client(40, 5).start();
        new Client(50, 5).start();
        new Client(60, 5).start();
        new Client(70, 5).start();
        new Client(80, 5).start();
        new Client(90, 5).start();
        new Client(100, 5).start();
    }
}
