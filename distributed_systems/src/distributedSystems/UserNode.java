package distributedSystems;

import org.javatuples.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

public class UserNode extends Thread{

    private ConsumerImp consumer;
    private PublisherImp publisher;

    /* Create socket for contacting the server on port 4321*/
    protected static Socket requestSocket = null;

    /* Create the streams to send and receive data from server */
    protected static ObjectOutputStream out = null;
    protected static ObjectInputStream in = null;
    protected static String ip;
    protected static int port;

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

    UserNode(ConsumerImp consuner, PublisherImp publisher){
        this.consumer=consuner;
        this.publisher=publisher;
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

    public void communicateWithBroker(String name){
        try{
            out.writeUTF("userNode");
            out.flush();
            out.writeUTF(name);
            out.flush();
            HashMap<String, Pair<String, Integer>> pairHashMap = (HashMap<String, Pair<String, Integer>>) in.readObject();
            this.topicWithBrokers = pairHashMap;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Socket init() {

        try {
            requestSocket = new Socket(this.getIp(),this.getPort());
            out= new ObjectOutputStream(requestSocket.getOutputStream());
            in= new ObjectInputStream(requestSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return requestSocket;
    }

    public static void main(String[] args) {

        int input = Integer.parseInt(args[0]);
        String[] initData = Util.initUserNode(input);
        String name = initData[1];
        ArrayList<String> topics = new ArrayList<>();
        HashMap<String, Queue<Message>> initConversations = new HashMap<>();
        String pathToUserNode = "data/usernode/"+name+"/";
        Queue<Message> tempQueue = new LinkedList<>();
        for(int i = 2; i < initData.length; i++){
            topics.add(initData[i]);
            initConversations.put(initData[i], Util.readConversationOfTopic(initData[i], pathToUserNode));
        }
        ProfileName profileName = new ProfileName(name, topics);
//        profileName.setProfileName("george");

        System.out.println(profileName);
        UserNode userNode = new UserNode("127.0.0.2",5001);
        Socket clientSocket  = userNode.init();
        userNode.communicateWithBroker(name);
        userNode.setConversation(initConversations);
        System.out.println(initConversations);
        System.out.println(userNode.getTopicWithBrokers());

        PublisherImp publisher = new PublisherImp(profileName);
        ConsumerImp consumer = new ConsumerImp(profileName);

        outerloop:
        while (true) {
            Scanner scanner = new Scanner(System.in);
//            topics.forEach(number->System.out.println(number));
            System.out.println("Choose topic");
            for(int i = 0; i<topics.size(); i++){
                System.out.println(i+". "+topics.get(i));
            }

            int selectedTopic = scanner.nextInt();
            System.out.println(
                            "\t0. To close the application!\n" +
                            "\t1. Send video\n" +
                            "\t2. Send image\n" +
                            "\t3. Send text\n" +
                            "\t4. Register consumer in broker!\n" +
                            "\t5. Read conversation!\n");
            int options = scanner.nextInt();
            switch (options) {
                case 0:
                    break outerloop;
                case 1:
                    System.out.println("Sending video!");
                    publisher.push("asfaleia", "example.mp4");
                    break;
                case 2:
                    System.out.println("Sending image!");
                    break;
                case 3:
                    publisher.notifyBrokersNewMessage("hello ");
                    System.out.println("Sending text!");
                    break;
                case 4:
                    consumer.register("george");
                    break;
                case 5:
                    consumer.showConversationData("asfaleia");
                    break;
                default:
                    System.out.println("Invalid action, please input a valid number!");
                    break;
            }
        }
    }
}
