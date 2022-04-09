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
            Message a = (Message) in.readObject();
//            int b = in.readInt();



            System.out.println("Got a: "+a);
            a.setMessage("peiragmeno mhnuma " + a.getMessage());
            out.writeObject(a);
            out.flush();
            System.out.println("Sent: "+(a.getMessage()));


        } catch (IOException | ClassNotFoundException e) {
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
