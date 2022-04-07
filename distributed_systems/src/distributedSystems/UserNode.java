package distributedSystems;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.Objects;
import java.util.Scanner;

import static java.util.Objects.hash;

public class UserNode extends Thread implements Node{

    Consumer consumer;
    Publisher publisher;
    UserNode(){}
    UserNode(Consumer cons, Publisher pub){
        this.consumer=cons;
        this.publisher=pub;
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

    public void run() {

    }
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

    @Override
    public void connect() {
        try {
            requestSocket = new Socket(ip, port);
//            out = new ObjectOutputStream(requestSocket.getOutputStream());
//            in = new ObjectInputStream(requestSocket.getInputStream());

            /* Write the two integers */
//            out.writeInt(3);
//            out.flush();

//            /* Print the received result from server */
//            System.out.println("Server>" + in.readInt());

        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        } catch (IOException ioException) {
            ioException.printStackTrace();
//        } finally {
//            try {
////                in.close(); out.close();
////                requestSocket.close();
//            } catch (IOException ioException) {
//                ioException.printStackTrace();
//            }
       }
    }

    @Override
    public void disconnect() {
        try {
            requestSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void init(String ip, int port) {
        connect();
        outerloop:
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println(
                    "0. To close the application!\n" +
                    "1. Send video\n" +
                    "2. Send image\n" +
                    "3. Send text\n");
            int options = scanner.nextInt();
            switch (options) {
                case 0:
                    disconnect();
                    break outerloop;
                case 1:
                    System.out.println("Sending video!");
                    break;
                case 2:
                    System.out.println("Sending image!");
                    break;
                case 3:
                    sendText(5);
                    System.out.println("Sending text!");
                    break;
                default:
                    System.out.println("Invalid action, please input a valid number!");
                    break;
            }
        }
    }

    public void sendText(int message){

        try {

            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            /* Write the two message */
            out.writeInt(message);
            out.flush();

            /* Print the received result from server */
            System.out.println("Server>" + in.readInt());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateNodes() {

    }


    public static void main(String[] args) {
//        System.out.println(Math.abs(Objects.hash("192.168.1.100_5000")) % 300);
//        System.out.println(Math.abs(Objects.hash("192.168.1.101_5001")) % 300);
//        System.out.println(Math.abs(Objects.hash("192.168.1.102_5002")) % 300);
//        System.out.println(Math.abs(Objects.hash("katanemimena_systimata")) % 300);
//        System.out.println(Math.abs(Objects.hash("karampelas")) % 300);
//        System.out.println(Math.abs(Objects.hash("magklaras")) % 300);
//        System.out.println(Math.abs(Objects.hash("trolinos")) % 300);
//        System.out.println(Math.abs(Objects.hash("xristoulakis")) % 300);
//        System.out.println(Math.abs(Objects.hash("sifis-antonis")) % 300);
//        System.out.println(Math.abs(Objects.hash("sifis")) % 300);
//        System.out.println(Math.abs(Objects.hash("magteo")) % 300);
////        System.out.println(Objects.hashCode("192.168.1.103_5003") % 1000);
////        System.out.println(Objects.hashCode("192.168.1.104_5004") % 1000);
////        System.out.println(Objects.hashCode("192.168.1.105_5005") % 1000);
////        System.out.println(Objects.hashCode("192.168.1.106_5006") % 1000);
//
//        System.out.println(Math.abs((hash("antonis-george") + hash("192.168.1.100_5000")) %1000 ) +"====="+ Math.abs(hash("192.168.1.100_5000") % 1000));
//        System.out.println(Math.abs((hash("katanemimena_systimata") + hash("192.168.1.100_5000")) %1000 ) +"====="+ Math.abs(hash("192.168.1.101_5001") % 1000));
//        System.out.println(Math.abs((hash("antonis") + hash("192.168.1.100_5000")) %1000 ) +"====="+ Math.abs(hash("192.168.1.102_5002") % 1000));
//
//
//
//        System.out.println(Math.abs(hash(sha1Hash("antonnis"))) % 1000);
//        System.out.println(Math.abs(hash(sha1Hash("192.168.1.100_5000"))) % 1000);
//        System.out.println(Math.abs(hash(sha1Hash("192.168.1.101_5001"))) % 1000);
//        System.out.println(Math.abs(hash(sha1Hash("192.168.1.102_5002"))) % 1000);
//
//        String b1h = sha1Hash("192.168.1.100_5000");
//        String b2h = sha1Hash("192.168.1.101_5001");
//        String b3h = sha1Hash("192.168.1.102_5002");
//        String an = sha1Hash("katanemimsasdfe");
//
//        System.out.println(b1h + "       " + an);
//
//        if (an.compareTo(b1h) < -1){
//            System.out.println(1);
//        }else if (an.compareTo(b3h) < -1){
//            System.out.println(3);
//        }else if (an.compareTo(b2h) < -1){
//            System.out.println(2);
//        }
//        else {
//            System.out.println(an.compareTo(b1h));
//            System.out.println(an.compareTo(b2h));
//            System.out.println(an.compareTo(b3h));
//        }


        UserNode userNode = new UserNode("127.0.0.1", 5000);

        userNode.init("127.0.0.1", 5000);
    }
}
