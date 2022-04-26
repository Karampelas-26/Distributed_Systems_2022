package distributedSystems;

import java.io.IOException;
import java.util.*;

public class ConsumerImp extends UserNode implements Consumer {

    private ProfileName profileName;
    private HashMap<String, Queue<Message>> conversation;

    public ConsumerImp() {
        this.conversation= new HashMap<>();
    }

    public ConsumerImp(ProfileName profileName) {
        this.profileName = profileName;
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
    public void showConversationData(String str) {
        try {
            out.writeUTF("consumer");
            out.flush();

            out.writeUTF("showConversation");
            out.flush();

            out.writeUTF(str);
            out.flush();

            int queueSize = in.readInt();
            Queue<Message> conversation = new LinkedList<>();

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
                    conversation.add(new Message(name, chunks));
                }
            }

            System.out.println(conversation);
        } catch (IOException | ClassNotFoundException e) {
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
