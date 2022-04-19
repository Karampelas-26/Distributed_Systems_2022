package distributedSystems;

import java.io.*;
import java.net.*;

public class ActionsForClients extends BrokerImp implements Runnable {
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

            String userType = in.readUTF();
            if (userType.equals("consumer")){
                String consumerAction = in.readUTF();
                if (consumerAction.equals("register")){
                    registerAConsumer(in.readUTF());
                    out.writeUTF("Successful user registration!");
                    out.flush();
                }
            }
            else {
                out.writeUTF("Server could recognize the client!");
                out.flush();
            }


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

    private void registerAConsumer(String name){
        registeredUsers.add(name);
    }
}
