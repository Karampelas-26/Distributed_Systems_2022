package distributedSystems;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class PublisherImp implements Publisher, Serializable {

    private ProfileName profileName;

    /* Create socket for contacting the server on port 4321*/
    Socket requestSocket = null;

    /* Create the streams to send and receive data from server */
    ObjectOutputStream outp = null;
    ObjectInputStream inp = null;
    private String ip;
    private int port;

    public ProfileName getProfileName() {
        return profileName;
    }

    public void setProfileName(ProfileName profileName) {
        this.profileName = profileName;
    }

    public Socket getRequestSocket() {
        return requestSocket;
    }

    public void setRequestSocket(Socket requestSocket) {
        this.requestSocket = requestSocket;
    }

    PublisherImp(){}
    PublisherImp(ProfileName profileName, Socket requestSocket){
        this.profileName=profileName;
        this. requestSocket = requestSocket;
        try {
            this.outp = new ObjectOutputStream(requestSocket.getOutputStream());
            this.inp = new ObjectInputStream(requestSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setStreams(){
        try {
            this.outp = new ObjectOutputStream(requestSocket.getOutputStream());
            this.inp = new ObjectInputStream(requestSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeStrams(){
        try {
            outp.close();
            inp.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Value> generateChunks(MultimediaFile multimediaFile) {
        return null;
    }

    @Override
    public void getBrokerList() {

    }

    /*

     */
    @Override
    public Broker hashTopic(String str) {
        return null;
    }

    @Override
    public void notifyBrokersNewMessage(String str) {
        try {

            System.out.println("mpike edw");
            /* Write the two message */
            outp.writeObject(new Message(str));
            outp.flush();
            System.out.println("kati tha eprepe na steilei logika");
            /* Print the received result from server */
            System.out.println("Server>" + inp.readInt());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

    @Override
    public void notifyFailure(Broker broker) {

    }

    @Override
    public void push(String topic, Value value) {

    }

    @Override
    public void connect() {
        System.out.println("trying to connect");

        try {
            requestSocket = new Socket("localhost", port);
            System.out.println("succes connection");

        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        } catch (IOException ioException) {
            ioException.printStackTrace();
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
        this.ip = ip;
        this.port = port;
        System.out.println("init");
//        connect();

    }

    @Override
    public void updateNodes() {

    }
}

class Message implements Serializable {

    String message;

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

