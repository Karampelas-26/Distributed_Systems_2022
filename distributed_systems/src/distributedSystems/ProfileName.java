package distributedSystems;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ProfileName implements Serializable {

    private String profileName;
    private ArrayList<String> subscribedConversations;

    public ProfileName(String profileName, ArrayList<String> subscribedConversations) {
        this.profileName = profileName;
        this.subscribedConversations = subscribedConversations;
    }

    public ProfileName() {
    }

    public ProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public ArrayList<String> getSubscribedConversations() {
        return subscribedConversations;
    }

    public void setSubscribedConversations(ArrayList<String> subscribedConversations) {
        this.subscribedConversations = subscribedConversations;
    }

    @Override
    public String toString() {
        return "ProfileName{" +
                "profileName='" + profileName + '\'' +
                ", subscribedConversations=" + subscribedConversations +
                '}';
    }
}
