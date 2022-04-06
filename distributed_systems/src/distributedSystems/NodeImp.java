package distributedSystems;

import java.util.List;

public class NodeImp implements Node{

    private List<Broker> brokers;

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

    public void addbBroker(Broker broker) {
        brokers.add(broker);
    }

}
