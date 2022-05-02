package distributedSystems;

public interface Consumer extends Node {

    public void disconnect(String str);

    public void register(String str);

    public void showConversationData(String str);
}
