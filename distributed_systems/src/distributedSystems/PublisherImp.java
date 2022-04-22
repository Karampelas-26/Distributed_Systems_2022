package distributedSystems;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class PublisherImp extends UserNode implements Publisher, Serializable {

    private ProfileName profileName;

    PublisherImp(){}
    PublisherImp(ProfileName profileName){
        this.profileName=profileName;
    }

    public ProfileName getProfileName() {
        return profileName;
    }

    public void setProfileName(ProfileName profileName) {
        this.profileName = profileName;
    }

    @Override
    public ArrayList<Value> generateChunks(MultimediaFile multimediaFile) {
        return null;
    }

    @Override
    public void getBrokerList() {

    }

    @Override
    public Broker hashTopic(String str) {
        return null;
    }

    @Override
    public void notifyBrokersNewMessage(String str) {
        try {

            System.out.println("mpike edw");
            /* Write the two message */
            out.writeUTF("publisher");
            out.flush();

            out.writeUTF("karampelas");
            out.flush();
            Message message =  new Message("hi");
            out.writeObject(message);
            out.flush();
            System.out.println("kati tha eprepe na steilei logika");
            /* Print the received result from server */
//            Message m  = (Message) inp.readObject();
//            System.out.println(inp.readObject().getClass().getName());
//            System.out.println("Server> " + inp.readUTF() );
        } catch (IOException e) {
            e.printStackTrace();
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
//        System.out.println("trying to connect");
//
//        try {
//            requestSocket = new Socket("localhost", port);
//            System.out.println("succes connection");
//
//        } catch (UnknownHostException unknownHost) {
//            System.err.println("You are trying to connect to an unknown host!");
//        } catch (IOException ioException) {
//            ioException.printStackTrace();
//        }
    }

    @Override
    public void disconnect() {
//        try {
//            requestSocket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void init(String ip, int port) {
//        this.ip = ip;
//        this.port = port;
//        System.out.println("init");
    }

    @Override
    public void updateNodes() {

    }
}
