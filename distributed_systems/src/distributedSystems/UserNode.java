package distributedSystems;

import org.javatuples.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

public class UserNode extends Thread{

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
        System.out.println(this.conversation.get(topic));
        System.out.println(this.conversation.get(topic).peek().getDate());
        Date date= this.conversation.get(topic).peek().getDate();
        System.out.println(this.conversation.get(topic));
        return date;
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

    public void redirect(String ip, int port){
        //close previous socket
        try{
            requestSocket.close();
            out.close();
            in.close();
            this.setIp(ip);
            this.setPort(port);
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * method to check if topic is at the broker we are currently connected
     * @param topic
     */
    public void checkBroker(String topic){
        String ip=topicWithBrokers.get(topic).getValue0();
        int port= topicWithBrokers.get(topic).getValue1();
        if(!(ip.equals(this.getIp()) && port==this.getPort())){
            System.out.println(ip+" "+ port+" "+ topic);
            redirect(ip,port);
        }
    }

    public static void main(String[] args) {

        int input = Integer.parseInt(args[0]);
        String[] initData = Util.initUserNode(input);
        String name = initData[1];
        ArrayList<String> topics = new ArrayList<>();
        HashMap<String, Queue<Message>> initConversations = new HashMap<>();
        String pathToUserNode = "data/usernode/"+name+"/";
        for(int i = 2; i < initData.length; i++){
            topics.add(initData[i]);
            initConversations.put(initData[i], Util.readConversationOfTopic(initData[i], pathToUserNode));
        }
        ProfileName profileName = new ProfileName(name, topics);

        System.out.println(profileName);
        UserNode userNode = new UserNode("127.0.0.2",5001);
        Socket clientSocket  = userNode.init();
        userNode.communicateWithBroker(name);
        userNode.setConversation(initConversations);
        System.out.println(userNode.getTopicWithBrokers());

        PublisherImp publisher = new PublisherImp(userNode, profileName);
        ConsumerImp consumer = new ConsumerImp(userNode, profileName);

        outerloop:
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Choose topic");
            String[] strTopics= new String[topics.size()];
            for(int i = 0; i<topics.size(); i++){
                System.out.println(i+". "+topics.get(i));
                strTopics[i]= topics.get(i);
            }

            int selectedTopic = scanner.nextInt();
            String displayTopic = strTopics[selectedTopic];
            userNode.getLastDate(displayTopic);
            userNode.checkBroker(displayTopic);
            System.out.println(
                            "\t0. To close the application!\n" +
                            "\t1. Send video\n" +
                            "\t2. Send image\n" +
                            "\t3. Send text\n" +
                            "\t4. Register consumer in broker!\n" +
                            "\t5. Read conversation!\n");
            int options = scanner.nextInt();
            scanner.nextLine();
            switch (options) {
                case 0:
                    break outerloop;
                case 1:
                    System.out.println("Please enter the path from the video: ");
                    String videoPath = scanner.nextLine();
                    publisher.push(displayTopic, videoPath);
                    break;
                case 2:
                    System.out.println("Please enter the path from the image: ");
                    String imagePath = scanner.nextLine();
                    publisher.push(displayTopic, imagePath);
                    break;
                case 3:
                    System.out.println("Enter your message: ");
                    publisher.notifyBrokersNewMessage("Enter your message: ");
                    break;
                case 4:
                    consumer.register("george");
                    break;
                case 5:
                    consumer.showConversationData(displayTopic);
                    break;
                default:
                    System.out.println("Invalid action, please input a valid number!");
                    break;
            }
        }
    }
}
