package distributedSystems;

public interface Broker extends Node {

    public Consumer acceptConnection(Consumer consumer);

    public Publisher acceptConnection(Publisher publisher);

    public void calculateKeys();

    public void filterConsumers(String str);

    public void notifyBrokersOnChanges();

    public void notifyPublisher(String str);

    public void pull(String str);

}
