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

//    public boolean check

    public void run() {
        try {
            /*
             *
             *
             *
             */
            Object a = in.readObject();
//            int b = in.readInt();
            if ( a instanceof Message) System.out.println("yeeees");


//            System.out.println("Got a: "+a);
////            ("peiragmeno mhnuma " + a);
//            out.writeUTF("hello from server to: " + a);
//            out.flush();
            System.out.println("Sent: "+a);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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
