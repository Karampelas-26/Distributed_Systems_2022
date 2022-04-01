package distributedSystems;

public interface Node {

    public void connect();

    public void disconnect();

    public void init(int something); //change the name of something

    public void updateNodes();

}
