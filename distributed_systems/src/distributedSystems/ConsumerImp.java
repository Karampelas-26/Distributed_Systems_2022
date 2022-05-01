package distributedSystems;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class ConsumerImp extends UserNode implements Consumer {

    private ProfileName profileName;
    private UserNode userNode;
    private HashMap<String, Queue<Message>> conversation;

    public ConsumerImp() {
        this.conversation= new HashMap<>();
    }

    public ConsumerImp(UserNode userNode, ProfileName profileName) {
        this.profileName = profileName;
        this.userNode = userNode;
        this.conversation= new HashMap<>();
    }

    @Override
    public void disconnect(String str) {

    }

    @Override
    public void register(String str) {
        try {
            System.out.println(out);
            System.out.println("trying to register in broker: " + ip + " " + port+ " with profile name: "+profileName);
            out.writeUTF("consumer");
            out.flush();

            out.writeUTF("register");
            out.flush();

            out.writeUTF(str);
            out.flush();
            System.out.println(in.readUTF());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showConversationData(String topic) {
        try {
            out.writeUTF("consumer");
            out.flush();

            out.writeUTF("showConversation");
            out.flush();

            out.writeUTF(topic);
            out.flush();

            Queue<Message> conversation = userNode.getConversation().get(topic);
            //elegxos gia an tha parei oli tin sunomilia i oxi
            if(conversation.isEmpty()){
                out.writeUTF("all");
                out.flush();
            }
            else{
                out.writeUTF("last");
                out.flush();
                System.out.println(conversation);
                out.writeObject(userNode.getLastDate(topic));
                out.flush();
            }

            int queueSize = in.readInt(); //posa minumata tha labei

            //lipsi minumatwn
            for(int i = 0; i < queueSize; i++){
                String typeOfMessage = in.readUTF();
                if(typeOfMessage.equals("s")){
                    Message message = (Message) in.readObject();
                    conversation.add(message);
                }
                else{
                    int numOfChunks = in.readInt();
                    ProfileName name  = (ProfileName) in.readObject();
                    List<MultimediaFile> chunks = new ArrayList<>();
                    for(int j = 0; j < numOfChunks; j++){
                        MultimediaFile multimediaFile = (MultimediaFile) in.readObject();
                        chunks.add(multimediaFile);
                    }
                    List<MultimediaFile> list = new ArrayList<>();
                    MultimediaFile finalFile = mergeMultimediaFiles(chunks);
                    createFile(finalFile);
                    list.add(finalFile);
                    conversation.add(new Message(name, list));
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void createFile(MultimediaFile fileToCreate){

        try (FileOutputStream fos = new FileOutputStream("data/usernode/"+this.profileName.getProfileName()+"/"+fileToCreate.getMultimediaFileName())) {
            fos.write(fileToCreate.getMultimediaFileChunk());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MultimediaFile mergeMultimediaFiles(List<MultimediaFile> chunks){
        int lenghtList = chunks.size();
        byte[] fileBytes = new byte[1024*16*lenghtList];
        for(int i = 0; i < lenghtList; i++){
            MultimediaFile multimediaFile = chunks.get(i);
            byte[] temp = multimediaFile.getMultimediaFileChunk();
            for(int j = 0; j < temp.length; j++){
                fileBytes[j + i*1024*16] = temp[j];
            }
        }
        MultimediaFile finalFile = new MultimediaFile();
        finalFile.setMultimediaFileName(chunks.get(0).getMultimediaFileName());
        finalFile.setMultimediaFileChunk(fileBytes);
        return finalFile;
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

    public ProfileName getProfileName() {
        return profileName;
    }

    public void setProfileName(ProfileName profileName) {
        this.profileName = profileName;
    }

    public HashMap<String, Queue<Message>> getConversation() {
        return conversation;
    }

    public void setConversation(HashMap<String, Queue<Message>> conversation) {
        this.conversation = conversation;
    }
}
