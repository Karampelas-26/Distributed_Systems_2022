package distributedSystems;

import java.util.List;

public interface Broker extends Node {


    public Consumer acceptConection(Consumer consumer);

    public Publisher acceptConnection(Publisher publisher);

    public void calculateKeys();

    public void filterConsumers(String str);

    public void notifyBrokersOnChanges();

    public void notifyPublisher(String str);

    public void pull(String str);

}
