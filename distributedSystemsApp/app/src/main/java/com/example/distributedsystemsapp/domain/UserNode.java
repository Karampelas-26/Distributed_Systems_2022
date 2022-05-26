package com.example.distributedsystemsapp.domain;

import org.javatuples.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class UserNode extends Thread implements Serializable {

    /* Create socket for contacting the server on port 4321*/
    protected static Socket requestSocket = null;

    /* Create the streams to send and receive data from server */
    protected static ObjectOutputStream out = null;
    protected static ObjectInputStream in = null;
    protected String ip;
    protected int port;

    private HashMap<String, Pair<String, Integer>> topicWithBrokers;
    private HashMap<String, Queue<Message>> conversation;


    public UserNode(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.topicWithBrokers = new HashMap<>();
        this.conversation = new HashMap<>();
    }

    UserNode(){
        this.topicWithBrokers = new HashMap<>();
        this.conversation = new HashMap<>();
    }

    public HashMap<String, Queue<Message>> getConversation() {
        return conversation;
    }

    public void setConversation(HashMap<String, Queue<Message>> conversation) {
        this.conversation = conversation;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }


    public void setPort(int port) {
        this.port = port;
    }

    public HashMap<String, Pair<String, Integer>> getTopicWithBrokers() {
        return topicWithBrokers;
    }

    public void setTopicWithBrokers(HashMap<String, Pair<String, Integer>> topicWithBrokers) {
        this.topicWithBrokers = topicWithBrokers;
    }

    public Date getLastDate(String topic){
        LinkedList<Message> ll = new LinkedList<>(this.conversation.get(topic));
        return ll.getLast().getDate();
    }

    public void communicateWithBroker(String name){
        try{
            out.writeUTF("userNode");
            out.flush();

            out.writeUTF("firstCommunication");
            out.flush();

            out.writeUTF(name);
            out.flush();

            this.topicWithBrokers = (HashMap<String, Pair<String, Integer>>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Socket init() {

        try {
            requestSocket = new Socket(InetAddress.getByName(this.getIp()),this.getPort());
            out= new ObjectOutputStream(requestSocket.getOutputStream());
            in= new ObjectInputStream(requestSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return requestSocket;

    }

    public void redirect(String ip, int port){
        //close previous socket
        closeWithServer();
        this.setIp(ip);
        this.setPort(port);
        init();
    }

    /**
     * method to check if topic is at the broker we are currently connected
     * @param topic
     */
    public void checkBroker(String topic){
        String ip=topicWithBrokers.get(topic).getValue0();
        int port= topicWithBrokers.get(topic).getValue1();
        if(!(ip.equals(this.getIp()) && port==this.getPort())){
            redirect(ip,port);
        }
    }

    public void closeWithServer(){
        try {
            out.writeUTF("close");
            out.flush();
            requestSocket.close();
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean registerUserAtTopic(String topic, String profileName){
        boolean status = false;
        try {
            out.writeUTF("userNode");
            out.flush();

            out.writeUTF("register");
            out.flush();

            out.writeUTF(topic);
            out.flush();

            out.writeUTF(profileName);
            out.flush();

            String response = in.readUTF();
            if(response.equals("success")){
                System.out.println("Successfully registered at " + topic + "!");
                conversation.put(topic, new LinkedList<>());
                status = true;
            }
            else {
                System.out.println("Failed registration at topic: " + topic + ", the topic does not exist!");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return status;
    }

//    public static void main(String[] args) {
//
//        int input = Integer.parseInt(args[0]);
//        String[] initData = Util.initUserNode(input);
//        String name = initData[1];
//        ArrayList<String> topics = new ArrayList<>();
//        HashMap<String, Queue<Message>> initConversations = new HashMap<>();
//        String pathToUserNode = "data/usernode/"+name+"/";
//        for(int i = 2; i < initData.length; i++){
//            topics.add(initData[i]);
//            initConversations.put(initData[i], Util.readConversationOfTopic(initData[i], pathToUserNode));
//        }
//        ProfileName profileName = new ProfileName(name, topics);
//
//        UserNode userNode = new UserNode("192.168.56.1",5000);
//        Socket clientSocket  = userNode.init();
//        userNode.communicateWithBroker(name);
//        userNode.setConversation(initConversations);
//        System.out.println("You have connected as: "+ profileName.getProfileName());
//
//        PublisherImp publisher = new PublisherImp(userNode, profileName);
//        ConsumerImp consumer = new ConsumerImp(userNode, profileName);
//
//        outerloop:
//        while (true) {
//            Scanner scanner = new Scanner(System.in);
//            System.out.println("Choose topic: ");
//            String[] strTopics= new String[topics.size()];
//            for(int i = 0; i<topics.size(); i++){
//                System.out.println(i+". "+topics.get(i));
//                strTopics[i]= topics.get(i);
//            }
//            System.out.println(strTopics.length+". Register at topic!\n");
//
//            int selectedTopic = scanner.nextInt();
//            scanner.nextLine();
//            if(selectedTopic==strTopics.length){
//                System.out.println("Enter the topic you want to register: ");
//                String topicToRegister = scanner.nextLine();
//                boolean response = userNode.registerUserAtTopic(topicToRegister,profileName.getProfileName());
//                if(response) {
//                    topics.add(topicToRegister);
//                    userNode.communicateWithBroker(name);
//                }
//            }
//            else{
//                String displayTopic = strTopics[selectedTopic];
//                userNode.checkBroker(displayTopic);
//                System.out.println(
//                                "\t0. To close the application!\n" +
//                                "\t1. Send video!\n" +
//                                "\t2. Send image!\n" +
//                                "\t3. Send text!\n" +
//                                "\t4. Open conversation!\n");
//                int options = scanner.nextInt();
//                scanner.nextLine();
//                switch (options) {
//                    case 0:
//                        userNode.closeWithServer();
//                        System.out.println(0);
//                        break outerloop;
//                    case 1:
//                        System.out.println("Please enter the path from the video: ");
//                        String videoPath = scanner.nextLine();
//                        publisher.push(displayTopic, videoPath);
//                        break;
//                    case 2:
//                        System.out.println("Please enter the path from the image: ");
//                        String imagePath = scanner.nextLine();
//                        publisher.push(displayTopic, imagePath);
//                        break;
//                    case 3:
//                        System.out.println("Enter your message: ");
//                        String message= scanner.nextLine();
//                        publisher.sendMessage(displayTopic,message);
//                        break;
//                    case 4:
//                        consumer.showConversationData(displayTopic);
//                        LinkedList<Message> conversation = (LinkedList<Message>) userNode.getConversation().get(displayTopic);
//                        SimpleDateFormat myFormatObj = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                        int conversationSize = conversation.size();
//                        for(int i = 0; i < conversationSize; i++){
//                            Message tempMessage = conversation.get(i);
//                            String strMessage = tempMessage.getMessage();
//                            if(tempMessage.getMessage() == null){
//                                strMessage = tempMessage.getFiles().get(0).getMultimediaFileName();
//                            }
//                            System.out.println(tempMessage.getName().getProfileName());
//                            String date = myFormatObj.format(tempMessage.getDate());
//                            System.out.println("Name: " + tempMessage.getName().getProfileName() + "\n" +
//                                    "Message: " + strMessage + "\n" +
//                                    "Date: " + date + "\n" +
//                                    "-------------------------------------------------------------------------------------------------------------------------------------------------------------"                            );
//                        }
//
//
//                        //while loop for live messages
//                        while(true){
//                            consumer.showConversationData(displayTopic);
//                            conversation = (LinkedList<Message>) userNode.getConversation().get(displayTopic);
//                            int differenceSize = conversation.size() - conversationSize;
//                            if(differenceSize > 0) {
//
//                                //typwnoume ta teleutaia mhnumata
//                                for(int i = 0; i < differenceSize; i++){
//                                    Message tempMessage = conversation.get(conversationSize + i);
//                                    String strMessage = tempMessage.getMessage();
//                                    if(tempMessage.getMessage() == null){
//                                        strMessage = tempMessage.getFiles().get(0).getMultimediaFileName();
//                                    }
//                                    System.out.println(tempMessage.getName().getProfileName());
//                                    String date = myFormatObj.format(tempMessage.getDate());
//                                    System.out.println("Name: " + tempMessage.getName().getProfileName() + "\n" +
//                                            "Message: " + strMessage + "\n" +
//                                            "Date: " + date + "\n" +
//                                            "-------------------------------------------------------------------------------------------------------------------------------------------------------------"                            );
//                                }
//
//                                conversationSize = conversation.size();//ananewnoume to prohgoumeno conversationSize
//
//                            }
//
//
//
//                        }
//                    default:
//                        System.out.println("Invalid action, please input a valid number!");
//                        break;
//                }
//            }
//        }
//    }
}
