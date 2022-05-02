package distributedSystems;

public interface Node {

    public void connect();

    public void disconnect();

    public void init(String ip, int something);

    public void updateNodes();

}
