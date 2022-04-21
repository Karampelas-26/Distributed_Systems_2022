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
    private HashMap<String, Integer> brokerIps= new HashMap<>();
    /* Define the socket that receives requests */
    private ServerSocket providerSocket;



    /* Define the socket that is used to handle the connection */
    private Socket connection = null;

    private String ip;
    private int port;

    private List<Triplet<Integer, ProfileName, HashSet<String>>> brokersPublisherTopics;

//    private brokersTopics = {broker: topic}
//
//    private publisherTopics = {topic: publisher}
//
//    private brokersPT = {<broker, publisher, topisdfbc>, <broker1,publisher,topic>}



    protected volatile static List<String> registeredUsers;

    private List<String> registeredPublishers;

    private List<Object[]> infoOfBrokers;


//    public void addSomeDummyData(int brokerID, ProfileName name, HashSet<String> topics){
//        brokersPublisherTopics.add(new Triplet<Integer, ProfileName, HashSet<String>>(brokerID, name, topics));
//    }


    public BrokerImp() {
        registeredUsers= new ArrayList<>();
    }

    public BrokerImp(String ip, int port) {
        this.ip = ip;
        this.port = port;
        registeredUsers= new ArrayList<String>();
    }

    public void addInfo(String ip, int port){
        brokerIps.put(ip,port);
    }


    //isws otan pame na kanoume connect ston server dld otan anoiksoume to socket
    //na steiloume prwta ti antikeimeno einai o autos poy theloume na einai o usernode
    //dld na steiloume publisher wste molis kanei accept o server na diabasei ti einai
    //kai na kanei acceptConnection kai na kanei diaforetika pragmata gia ton kathena

    /*
    //edw tha ulopoihsoume ti tha kanei o server otan kanei accept to connection kai einai o consumer
     */
    @Override
    public Consumer acceptConnection(Consumer consumer) {
        return null;
    }

    /*
    //edw tha ulopoihsoume ti tha kanei o server otan kanei accept to connection kai einai o publisher
     */
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

//            InetAddress addr = InetAddress.getByName("127.0.0.1");
            providerSocket = new ServerSocket(port);

            while (true) {
                /* Accept the connection */

                //otan tha kanei accept o server mporoume na to ulopoihsoume se mia
                //SocketHandler wste na trexoume parallila kathe connection

//                for (;;) {
//                    SocketHandler socketHander = new SocketHandler(serverSocket.accept());
//                    socketHander.start();
//                }


                //ston connection tou consumer steile pisw ta tuples

                connection = providerSocket.accept();

                /* Handle the request */
                Thread t = new Thread(new ActionsForClients(connection));
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

    public static void main(String[] args) {

        HashMap<String,Integer> borokers = Util.readAllBrokersFromConfToHashMap();
        HashSet<String> topics = Util.readAllTopicsFromConf();

        int brokerID = Integer.parseInt(args[0]);
        BrokerImp broker = new BrokerImp();

        Queue<Message> conversation =  Util.readConversationOfTopic("asfaleia");
        int size = conversation.size();
        for(int i = 0; i < size; i++){
            System.out.println(conversation.remove());
        }

//        comment the block below for null this.brokersPublisherTopics idk why...George
//        HashSet<String> d = new HashSet<String>();
//        d.add("katanemimena");
//        d.add("magteo");
//        d.add("antonis");
//        broker.addSomeDummyData(1, new ProfileName("goerge"),  d);
//        HashSet<String> dS = new HashSet<String>();
//        d.add("GFSD");
//        d.add("magtASDFeo");
//        d.add("antoASDFnis");
//        broker.addSomeDummyData(2, new ProfileName("ANTONARAS"),  dS);

//        String str2 = new String(Util.topicToSHA1Hash("127.0.0.1" + "5000"));
//        for(String t: topics){
//            String str1 = new String(Util.topicToSHA1Hash(t));
//
//            if(str1.compareTo(str2) > 0) {
//                System.out.println("Smaller");
//            }
//        }
//        System.out.println(Util.topicToSHA1Hash("127.0.0.1"));
//        System.out.println(Util.topicToSHA1Hash("5000"));
//        System.out.println(Util.topicToSHA1Hash("127.0.0.1" + "5000"));


        System.out.println("The server running is: " + args[0]);
        Pair<String, Integer> brokerInfo = Util.findIPAddressAndPortOfBroker(brokerID);
        broker.init(brokerInfo.getValue0(), brokerInfo.getValue1());
    }

    private static String sha1Hash(String value){

        String sha1 = "";

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(value.getBytes("utf8"));
            sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
        } catch (Exception e){
            e.printStackTrace();
        }

        return sha1;
    }
}