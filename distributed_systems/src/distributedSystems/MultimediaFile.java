package distributedSystems;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

public class MultimediaFile implements Serializable {

    private String multimediaFileName, profileName, dateCreated, length, framerate, frameWidth, frameHeight;
    private byte[] multimediaFileChunk;

    public MultimediaFile(String multimediaFileName, String profileName, String dateCreated, String length, String framerate, String frameWidth, String frameHeight, byte[] multimediaFileChunk) {
        this.multimediaFileName = multimediaFileName;
        this.profileName = profileName;
        this.dateCreated = dateCreated;
        this.length = length;
        this.framerate = framerate;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.multimediaFileChunk = multimediaFileChunk;
    }

    public MultimediaFile(String multimediaFileName, String profileName){
        this.multimediaFileName = multimediaFileName;
        this.profileName = profileName;
        this.dateCreated = new Date().toString();
        this.length = Long.toString(new File(multimediaFileName).length());
        this.multimediaFileChunk = Util.loadFile(multimediaFileName);
    }

    public MultimediaFile(String multimediaFileName, String profileName, byte[]multimediaFileChunk){
        this.multimediaFileName = multimediaFileName;
        this.profileName = profileName;
        this.dateCreated = new Date().toString();
        this.length = Long.toString(new File(multimediaFileName).length());
        this.multimediaFileChunk = multimediaFileChunk;
    }

    public MultimediaFile() {
    }

    public String getMultimediaFileName() {
        return multimediaFileName;
    }

    public void setMultimediaFileName(String multimediaFileName) {
        this.multimediaFileName = multimediaFileName;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getFramerate() {
        return framerate;
    }

    public void setFramerate(String framerate) {
        this.framerate = framerate;
    }

    public String getFrameWidth() {
        return frameWidth;
    }

    public void setFrameWidth(String frameWidth) {
        this.frameWidth = frameWidth;
    }

    public String getFrameHeight() {
        return frameHeight;
    }

    public void setFrameHeight(String frameHeight) {
        this.frameHeight = frameHeight;
    }

    public byte[] getMultimediaFileChunk() {
        return multimediaFileChunk;
    }

    public void setMultimediaFileChunk(byte[] multimediaFileChunk) {
        this.multimediaFileChunk = multimediaFileChunk;
    }
}
