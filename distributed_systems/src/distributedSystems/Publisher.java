package distributedSystems;

import java.util.ArrayList;

public interface Publisher extends Node {

    public ArrayList<Value> generateChunks(MultimediaFile multimediaFile);

    public void getBrokerList();

    public Broker hashTopic(String str);

    public void notifyBrokersNewMessage(String str);

    public void notifyFailure(Broker broker);

    public void push(String str, Value value);

}
