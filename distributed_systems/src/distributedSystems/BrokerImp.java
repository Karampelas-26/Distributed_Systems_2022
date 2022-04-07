package distributedSystems;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class BrokerImp implements Broker{
    static HashMap<String, Integer> brokerIps= new HashMap<>();
    /* Define the socket that receives requests */
    ServerSocket providerSocket;

    /* Define the socket that is used to handle the connection */
    Socket connection = null;

    private List<Consumer> registeredUsers;

    private List<Publisher> registeredPublishers;

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
            providerSocket = new ServerSocket(port, 10);

            while (true) {
                /* Accept the connection */
                connection = providerSocket.accept();

                /* Handle the request */
//                Thread t = new ActionsForClients(connection);
//                t.start();




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
}
