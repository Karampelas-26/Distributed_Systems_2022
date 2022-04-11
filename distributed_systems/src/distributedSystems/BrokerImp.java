package distributedSystems;

import org.javatuples.Triplet;

import java.io.File;
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



    private List<Consumer> registeredUsers;

    private List<Publisher> registeredPublishers;

    private List<Object[]> infoOfBrokers;


    public void addSomeDummyData(int brokerID, ProfileName name, HashSet<String> topics){
        brokersPublisherTopics.add(new Triplet<Integer, ProfileName, HashSet<String>>(brokerID, name, topics));
    }


    public BrokerImp() {
    }

    public BrokerImp(String ip, int port) {
        this.ip = ip;
        this.port = port;
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
                Thread t = new ActionsForClients(connection);
//                t.checkUser();

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

    public static void main(String[] args) {

        String brokerID = args[0];
        BrokerImp broker = new BrokerImp();
        HashSet<String> d = new HashSet<String>();
        d.add("katanemimena");
        d.add("magteo");
        d.add("antonis");
        broker.addSomeDummyData(1, new ProfileName("goerge"),  d);
        HashSet<String> dS = new HashSet<String>();
        d.add("GFSD");
        d.add("magtASDFeo");
        d.add("antoASDFnis");
        broker.addSomeDummyData(2, new ProfileName("ANTONARAS"),  dS);


        System.out.println("The server running is: " + args[0]);

        try {

            File file = new File("src/distributedSystems/conf.txt");
            Scanner scanner =  new Scanner(file);
            String brokerip=null;
            int brokerport=0;
            while (scanner.hasNextLine()){
                String data = scanner.nextLine();
                String[] info = data.split(",");
                broker.addInfo(info[1],Integer.parseInt(info[2]));
                if (brokerID.equals(info[0])) {
                    brokerip=info[1];
                    brokerport=Integer.parseInt(info[2]);
                }
            }
            broker.init(brokerip, brokerport);

        } catch (Exception e) {
            e.printStackTrace();
        }
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

//class SocketHandler extends Thread {
//        private Socket socket;
//
//        public SocketHandler(Socket socket) {
//            this.socket = socket;
//        }
//
//        public void run() {
//
//
//
//
//        }
//}
