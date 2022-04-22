package distributedSystems;

import java.io.Serializable;

public class Message implements Serializable {

    String message;
    ProfileName name;

    public Message(String message) {
        this.message = message;
    }

    public Message(String message, ProfileName name){
        this.message=message;
        this.name=name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                '}';
    }
}