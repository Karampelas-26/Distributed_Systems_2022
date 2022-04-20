package distributedSystems;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ActionsForClients extends BrokerImp implements Runnable {
    ObjectInputStream in;
    ObjectOutputStream out;

    public ActionsForClients(Socket connection) {
        try {
            System.out.println("Got a connection...Opening streams....");
            out = new ObjectOutputStream(connection.getOutputStream());
            in = new ObjectInputStream(connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(true) {
            try {
                String userType = in.readUTF();
                if (userType.equals("consumer")) {
                    String consumerAction = in.readUTF();
                    if (consumerAction.equals("register")) {
                        registerAConsumer(in.readUTF());
                        out.writeUTF("Successful user registration!");
                        out.flush();
                    }
                } else if (userType.equals("publisher")) {
                    String publisherAction = in.readUTF();
                    if (publisherAction.equals("karampelas")) {
                        try {
                            Message m = (Message) in.readObject();
                            System.out.println(m.toString());
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    out.writeUTF("Server could not recognize the client!");
                    out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerAConsumer(String name){
        registeredUsers.add(name);
    }
}
