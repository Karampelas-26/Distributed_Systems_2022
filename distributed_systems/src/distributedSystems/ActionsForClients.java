package distributedSystems;

import java.io.*;
import java.net.*;

public class ActionsForClients extends Thread {
    ObjectInputStream in;
    ObjectOutputStream out;

    public ActionsForClients(Socket connection) {
        try {
            /*
             *
             *
             *
             */
            System.out.println("Got a connection...Opening streams....");
            out = new ObjectOutputStream(connection.getOutputStream());
            in = new ObjectInputStream(connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            /*
             *
             *
             *
             */
            int a = in.readInt();
//            int b = in.readInt();
            System.out.println("Got a: "+a);
            out.writeInt(a + a);
            out.flush();
            System.out.println("Sent: "+(a+a));


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
