package distributedSystems;

import java.util.ArrayList;

public class PublisherImp implements Publisher{

    private ProfileName profileName;

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

    }

    @Override
    public void notifyFailure(Broker broker) {

    }

    @Override
    public void push(String str, Value value) {

    }

    @Override
    public void connect() {
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void init(int something) {

    }

    @Override
    public void updateNodes() {

    }
}
