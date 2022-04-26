package distributedSystems;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Message implements Serializable {

    private String message;
    private ProfileName name;
    private List<MultimediaFile> files;

    public Message(String message) {
        this.message = message;
    }

    public Message(List<MultimediaFile> files){
        this.files = files;
    }

    public Message(String message, ProfileName name){
        this.message=message;
        this.name=name;
    }

    public Message(ProfileName name, List<MultimediaFile> files) {
        this.name = name;
        this.files = files;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ProfileName getName() {
        return name;
    }

    public void setName(ProfileName name) {
        this.name = name;
    }

    public List<MultimediaFile> getFiles() {
        return files;
    }

    public void setFiles(List<MultimediaFile> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", name=" + name +
                ", files=" + files +
                '}';
    }
}