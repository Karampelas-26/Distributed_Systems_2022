package distributedSystems;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
                System.out.println(userType);
                if (userType.equals("consumer")) {
                    String consumerAction = in.readUTF();
                    if (consumerAction.equals("register")) {
                        registerAConsumer(in.readUTF());
                        out.writeUTF("Successful user registration!");
                        out.flush();
                    }
                } else if (userType.equals("publisher")) {
                    System.out.println("in publisher");
                    String publisherAction = in.readUTF();
                    if (publisherAction.equals("karampelas")) {
                        try {
                            Message m = (Message) in.readObject();
                            System.out.println(m.toString());
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (publisherAction.equals("multimediaFile")){
                        try{
                            System.out.println("in multimediaFile");
                            String topic = in.readUTF();
                            int chunks = in.readInt();
                            List<MultimediaFile> fileInChunks = new ArrayList<>();
                            for(int i = 0; i < chunks; i++){
                                MultimediaFile multimediaFile = (MultimediaFile) in.readObject();
                                fileInChunks.add(multimediaFile);
                                System.out.println(i + "____"+ multimediaFile.getMultimediaFileChunk());

                            }
                            System.out.println(fileInChunks);
                            addMessageInConversation(topic, new Message(fileInChunks));
                        } catch (IOException e) {
                            e.printStackTrace();
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

    private void addMessageInConversation(String topic, Message message){
        Queue<Message> q = new LinkedList<>();
        q.add(new Message("hi"));
        q.add(new Message("hello"));
        conversations.put("asfaleia",q);
        System.out.println(conversations.get(topic));

        conversations.get(topic).add(message);
        System.out.println(conversations.get(topic));
    }
}
