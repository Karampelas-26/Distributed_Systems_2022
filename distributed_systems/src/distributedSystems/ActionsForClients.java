package distributedSystems;

import org.javatuples.Pair;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class ActionsForClients extends BrokerImp implements Runnable {
    ObjectInputStream in;
    ObjectOutputStream out;
    BrokerImp broker;


    public ActionsForClients(BrokerImp broker,Socket connection) {
        this.broker = broker;
        try {
            System.out.println("Got a connection...Opening streams....");
            out = new ObjectOutputStream(connection.getOutputStream());
            in = new ObjectInputStream(connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void consumer(){
        try {
            String consumerAction = in.readUTF();
            if (consumerAction.equals("register")) {
                registerAConsumer(in.readUTF());
                out.writeUTF("Successful user registration!");
                out.flush();
            }
            else if(consumerAction.equals("showConversation")){
                System.out.println("in conversation");
                String topic = in.readUTF();
                if(topic.equals("asfaleia")){ //check if consumer has access on topic and if topic exists
                    System.out.println(broker.getRegisteredUsers());
                    System.out.println(broker.getConversations());
                    System.out.println("inasfaleia");
                    Queue<Message> conversation = new LinkedList<>(broker.getConversations().get(topic));
                    int sizeOfQueue = conversation.size();
                    out.writeInt(sizeOfQueue);
                    out.flush();
                    for(int i = 0; i < sizeOfQueue; i++){
                        Message message = conversation.poll();
                        if(message.getMessage() == null){
                            out.writeUTF("f");
                            out.flush();
                            List<MultimediaFile> files = message.getFiles();
                            int sizeOfFiles = files.size();
                            out.writeInt(sizeOfFiles);
                            out.flush();
                            out.writeObject(message.getName());
                            out.flush();
                            for(int j = 0; j < sizeOfFiles; j++){
                                out.writeObject(files.get(j));
                                out.flush();
                            }
                        }
                        else {
                            out.writeUTF("s");
                            out.flush();
                            out.writeObject(message);
                            out.flush();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void publisher(){
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void userNode(){
        try {
            String name = in.readUTF();

            //this will return
            HashMap<String, Pair<String, Integer>> topicForUserNode = new HashMap<>();

            //we need them to work
            HashMap<String, String> topicOfBrokers = broker.getTopicsOfBrokers();
            HashMap<String, Integer> brokerIps = broker.getBrokerIps();
            ArrayList<String> topics = broker.getUsersAtTopic().get(name);
            for(int i = 0; i < topics.size(); i++){
                String ip = topicOfBrokers.get(topics.get(i));
                int port = brokerIps.get(ip);
                Pair<String, Integer> broker = new Pair<>(ip, port);
                topicForUserNode.put(topics.get(i), broker);
            }
            out.writeObject(topicForUserNode);
            out.flush();
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
                    consumer();
                } else if (userType.equals("publisher")) {
                    publisher();
                }
                else if (userType.equals("userNode")) {
                    userNode();
                }
                else{
                    out.writeUTF("Server could not recognize the client!");
                    out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerAConsumer(String name){
        broker.increaseRegisteredUser(name);
    }

    //thelei diorthwsi me to conversations allagi se broker.setConversations kai to name einai null
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
