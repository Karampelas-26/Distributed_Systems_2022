package distributedSystems;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.*;

public class BrokerImp implements Broker{

    private HashMap<String, Integer> brokerIps= new HashMap<>(); //list with all brokers
    /* Define the socket that receives requests */
    private ServerSocket providerSocket;
    /* Define the socket that is used to handle the connection */
    private Socket connection = null;
    private String ip;
    private int port;
    protected volatile List<String> registeredUsers;
    protected volatile HashMap<String, Queue<Message>> conversations;

    private List<String> registeredPublishers;
    private HashMap<String,String> topicsOfBrokers; //topic and brokers
    private HashMap<String, ArrayList<String>> usersAtTopic; //user and his topics

    public BrokerImp() {
        registeredUsers= new ArrayList<>();
        topicsOfBrokers= new HashMap<>();
        conversations = new HashMap<>();
        usersAtTopic = new HashMap<>();
    }

    public BrokerImp(HashMap<String, String> topicsOfBrokers, HashMap<String, Integer> brokerIps, HashMap<String, ArrayList<String>> usersAtTopic) {
        registeredUsers= new ArrayList<>();
        this.topicsOfBrokers= topicsOfBrokers;
        this.conversations = new HashMap<>();
        this.brokerIps = brokerIps;
        this.usersAtTopic = usersAtTopic;
    }

    public BrokerImp(String ip, int port) {
        this.ip = ip;
        this.port = port;
        registeredUsers= new ArrayList<>();
        topicsOfBrokers= new HashMap<>();
        conversations = new HashMap<>();
        usersAtTopic = new HashMap<>();
    }

    public void addInfo(String ip, int port){
        brokerIps.put(ip,port);
    }

    @Override
    public Consumer acceptConnection(Consumer consumer) {
        return null;
    }

    @Override
    public Publisher acceptConnection(Publisher publisher) {
        return null;
    }

    @Override
    public void calculateKeys() {

    }

    @Override
    public void filterConsumers(String str) {

    }

    @Override
    public void notifyBrokersOnChanges() {

    }

    @Override
    public void notifyPublisher(String str) {

    }

    @Override
    public void pull(String str) {

    }

    @Override
    public void connect() {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public void init(String ip, int port) {

        System.out.println(ip + "==="+ port);

        try {
            /* Create Server Socket */
            providerSocket = new ServerSocket(port);

            while (true) {
                /* Accept the connection */

                connection = providerSocket.accept();

                /* Handle the request */
                Thread t = new Thread(new ActionsForClients(this, connection));
                t.start();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            try {
                providerSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    @Override
    public void updateNodes() {

    }

    public void addRegisterUser(String name){
        registeredUsers.add(name);
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

    public HashMap<String,String> getTopicsOfBrokers() {
        return topicsOfBrokers;
    }

    public void setTopicsOfBrokers(HashMap<String,String> topicsOfBrokers) {
        this.topicsOfBrokers = topicsOfBrokers;
    }

    public HashMap<String, Integer> getBrokerIps() {
        return brokerIps;
    }

    public void setBrokerIps(HashMap<String, Integer> brokerIps) {
        this.brokerIps = brokerIps;
    }

    public List<String> getRegisteredPublishers() {
        return registeredPublishers;
    }

    public void setRegisteredPublishers(List<String> registeredPublishers) {
        this.registeredPublishers = registeredPublishers;
    }

    public HashMap<String, Queue<Message>> getConversations() {
        return this.conversations;
    }

    public void setConversations(HashMap<String, Queue<Message>> conversations) {
        this.conversations = conversations;
    }

    public void addMessageOnConversation(String topic, Message message){
        this.conversations.get(topic).add(message);
    }

    public List<String> getRegisteredUsers() {
        return registeredUsers;
    }

    public void setRegisteredUsers(List<String> registeredUsers) {
        this.registeredUsers = registeredUsers;
    }

    public void increaseRegisteredUser(String name){
        this.registeredUsers.add(name);
    }

    public HashMap<String, ArrayList<String>> getUsersAtTopic() {
        return usersAtTopic;
    }

    public void setUsersAtTopic(HashMap<String, ArrayList<String>> usersAtTopic) {
        this.usersAtTopic = usersAtTopic;
    }

    public static void main(String[] args) {

        int brokerID = Integer.parseInt(args[0]);
        BrokerImp broker = new BrokerImp(Util.readAllBrokerTopicsFromConf(), Util.readAllBrokersFromConfToHashMap(), Util.getUsersAtTopic());

        System.out.println("The server running is: " + args[0]);
        Pair<String, Integer> brokerInfo = Util.findIPAddressAndPortOfBroker(brokerID);

        //temp vars for init conversations
        HashMap<String, Queue<Message>> conversation = new HashMap<>();
        String pathOfConversations = "data/broker/conversations/";

        for(Map.Entry<String, String> topic: broker.getTopicsOfBrokers().entrySet()){
            if(topic.getValue().equals(brokerInfo.getValue0())){
                conversation.put(topic.getKey(), Util.readConversationOfTopic(topic.getKey(), pathOfConversations));
            }
        }
        //init conversations and broker
        broker.setConversations(conversation);
        broker.init(brokerInfo.getValue0(), brokerInfo.getValue1());
    }
}