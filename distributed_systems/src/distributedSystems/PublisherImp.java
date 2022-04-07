package distributedSystems;

import java.util.ArrayList;

public class PublisherImp implements Publisher{

    private ProfileName profileName;

    PublisherImp(){}
    PublisherImp(ProfileName profileName){
        this.profileName=profileName;
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

    }

    @Override
    public void notifyFailure(Broker broker) {

    }

    @Override
    public void push(String topic, Value value) {

    }

    @Override
    public void connect() {
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void init(String ip, int port) {

    }

    @Override
    public void updateNodes() {

    }
}
