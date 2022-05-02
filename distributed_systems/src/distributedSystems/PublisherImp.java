package distributedSystems;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PublisherImp extends UserNode implements Publisher, Serializable {

    private ProfileName profileName;
    private UserNode userNode;

    PublisherImp(){}
    PublisherImp(UserNode userNode, ProfileName profileName){
        this.profileName=profileName;
        this.userNode = userNode;
    }

    public ProfileName getProfileName() {
        return profileName;
    }

    public void setProfileName(ProfileName profileName) {
        this.profileName = profileName;
    }

    @Override
    public ArrayList<MultimediaFile> generateChunks(String nameOfFile) {

        byte[] file = Util.loadFile(nameOfFile);
        String[] onlyFileName = nameOfFile.split("\\\\");
        String fileName = onlyFileName[onlyFileName.length-1];
        List<byte[]> listOfChunks = Util.splitFileToChunks(file, 1024*16);
        int numOfChunks = listOfChunks.size();
        ArrayList<MultimediaFile> listOfMultimediaFiles = new ArrayList<>();

        for (int i = 0; i < numOfChunks; i++){
            byte[] tempArr = listOfChunks.get(i);
            MultimediaFile tempFile = new MultimediaFile(fileName, getProfileName().getProfileName(), tempArr.length, tempArr);
            listOfMultimediaFiles.add(tempFile);
        }
        return listOfMultimediaFiles;
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

    }

    public void sendMessage(String topic, String message){
        try {
            out.writeUTF("publisher");
            out.flush();

            out.writeUTF("sendMessage");
            out.flush();

            Message messageToSend = new Message(message, this.profileName);

            out.writeUTF(topic);
            out.flush();

            out.writeObject(messageToSend);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyFailure(Broker broker) {

    }

    @Override
    public void push(String topic, String nameOfFile) {
        ArrayList<MultimediaFile> chunks = this.generateChunks(nameOfFile);
        int numOfChunks = chunks.size();
        try {
            out.writeUTF("publisher");
            out.flush();

            out.writeUTF("multimediaFile");
            out.flush();

            out.writeUTF(topic);
            out.flush();

            out.writeObject(getProfileName());
            out.flush();

            out.writeInt(numOfChunks);
            out.flush();

            for (int i = 0; i < numOfChunks; i++) {
                out.writeObject(chunks.get(i));
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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