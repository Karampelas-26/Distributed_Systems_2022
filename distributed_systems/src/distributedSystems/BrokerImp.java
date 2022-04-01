package distributedSystems;

import java.util.List;

public class BrokerImp implements Broker{

    private List<Consumer> registeredUsers;

    private List<Publisher> registeredPublishers;

    @Override
    public Consumer acceptConection(Consumer consumer) {
        return null;
    }

    @Override
    public Publisher acceptConnection(Publisher publisher) {
        return null;
    }

    @Override
    public void calculateKeys() {

    }

    @Override
    public void filterConsumers(String str) {

    }

    @Override
    public void notifyBrokersOnChanges() {

    }

    @Override
    public void notifyPublisher(String str) {

    }

    @Override
    public void pull(String str) {

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
