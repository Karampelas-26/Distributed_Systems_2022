package distributedSystems;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class UserNode extends Thread{

    private ConsumerImp consumer;
    private PublisherImp publisher;


    /* Create socket for contacting the server on port 4321*/
    Socket requestSocket = null;

    /* Create the streams to send and receive data from server */
    ObjectOutputStream out = null;
    ObjectInputStream in = null;
    private String ip;
    private int port;

    public UserNode(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    UserNode(){}

    UserNode(ConsumerImp consuner, PublisherImp publisher){
        this.consumer=consuner;
        this.publisher=publisher;
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


    public void run() {

    }


    public void sendText(int message){

//        try {
//
//            out = new ObjectOutputStream(requestSocket.getOutputStream());
//            in = new ObjectInputStream(requestSocket.getInputStream());
//
//            /* Write the two message */
//            out.writeInt(message);
//            out.flush();
//
//            /* Print the received result from server */
//            System.out.println("Server>" + in.readInt());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                in.close();
//                out.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public Socket init() {


        try {
            requestSocket = new Socket("127.0.0.1", 5000);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return requestSocket;
        }


    }

    public static void main(String[] args) {


        ProfileName profileName = new ProfileName();
        profileName.setProfileName("george");

        UserNode userNode = new UserNode("192.168.1.101",5001);
        Socket clientSocket  = userNode.init();

        PublisherImp publisher = new PublisherImp(profileName, clientSocket);
        ConsumerImp consumer = new ConsumerImp(profileName, clientSocket);


        outerloop:
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println(
                    "0. To close the application!\n" +
                            "1. Send video\n" +
                            "2. Send image\n" +
                            "3. Send text\n" +
                            "4. Register consumer in broker!\n");
            int options = scanner.nextInt();
//            int options = 3;
            switch (options) {
                case 0:
                    break outerloop;
                case 1:
                    System.out.println("Sending video!");
                    break;
                case 2:
                    System.out.println("Sending image!");
                    break;
                case 3:
//                    userNode.init();
                    publisher.notifyBrokersNewMessage("hello ");
                    System.out.println("Sending text!");
                    break;
                case 4:
                    consumer.register("george");
                    break;
                default:
                    System.out.println("Invalid action, please input a valid number!");
                    break;
            }
        }
    }
}
