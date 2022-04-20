package distributedSystems;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ConsumerImp extends UserNode implements Consumer {

    private ProfileName profileName;

    public ConsumerImp() {

    }

    public ConsumerImp(ProfileName profileName) {
        this.profileName = profileName;
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
    public void showConversationData(String str, Value value) {

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
