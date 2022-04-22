package distributedSystems;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.*;

public final class Util {

    private static final String PATH = "src/distributedSystems/conf.txt";
    private static final String FOLDER_PATH = "src/distributedSystems/";
    /**
     * Reading from the file conf.txt information about brokers
     * @return An Arraylist with Triplets<BrokerID, IP Address, Port>
     */
    public static ArrayList<Triplet<Integer, String, Integer>> readAllBrokersFromConfToArrayListOfTtriplets() {

        ArrayList<Triplet<Integer, String, Integer>> brokers = new ArrayList<>();
        File file = new File(PATH);
        Scanner line = null;
        try {
            line = new Scanner(file);
            if (!line.nextLine().equals("Brokers")) return null;//check for possible error in file
            while (line.hasNextLine()){
                String data = line.nextLine();
                if (data.equals("Topics")) break; //break the while and stop reading file
                String info[] = data.split(",");
                Triplet<Integer, String, Integer> brokerInforamtion = new Triplet<Integer, String, Integer>(Integer.parseInt(info[0]), info[1], Integer.parseInt(info[2]));
                brokers.add(brokerInforamtion);
            }
            line.close();//close the file stream
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return brokers;

    }

    /**
     * Reading from the file conf.txt information about brokers
     * @return A HashMap with IP Address and Port of brokers
     */
    public static HashMap<String, Integer> readAllBrokersFromConfToHashMap(){

        HashMap<String, Integer> brokers = new HashMap<>();
        File file = new File(PATH);
        Scanner line = null;
        try {
            line = new Scanner(file);
            if (!line.nextLine().equals("Brokers")) return null;//check for possible error in file
            while (line.hasNextLine()){
                String data = line.nextLine();
                if (data.equals("Topics")) break; //break the while and stop reading file
                String info[] = data.split(",");
                brokers.put(info[1], Integer.parseInt(info[2]));
            }
            line.close();//close the file stream
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return brokers;
    }

    /**
     * reading all topics from the configuration file
     * @return HashSet
     */
    public static HashSet<String> readAllTopicsFromConf(){

        HashSet<String> topics = new HashSet<>();
        try {
            File file = new File(PATH);
            Scanner line = new Scanner(file);
            boolean topicFound = false;
            while (line.hasNextLine()) {
                String data = line.nextLine();
                if (data.equals("Topics")){
                    topicFound = true;
                    continue;
                }
                if(topicFound) {
                    topics.add(data);
                }
                else {
                    continue;
                }
            }
            line.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return topics;
    }


    /**
     * based on id it finds the address and the port of a broker
     * @param brokerID
     * @return
     */
    public static Pair<String, Integer> findIPAddressAndPortOfBroker(int brokerID){

        Pair<String, Integer> brokerInfo = null;

        try {
            File file = new File(PATH);
            Scanner line = new Scanner(file);
            while (line.hasNextLine()){
                String data = line.nextLine();
                if(data.equals("Brokers")) continue;
                else if (data.equals("Topics")) break;
                String info[] = data.split(",");
                if (brokerID == Integer.parseInt(info[0])){
                    brokerInfo = new Pair<>(info[1], Integer.parseInt(info[2]));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return brokerInfo;
    }

    /**
     * creating hash value of a String with SHA-1
     * @param value
     * @return
     */
    public static String topicToSHA1Hash(String value){

        String sha1 = "";

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(value.getBytes("utf8"));
            sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
        } catch (Exception e){
            e.printStackTrace();
        }
        return sha1;
    }

    /**
     * methodos gia na epistrepsei to HashMap me ta topic
     * kai poios broker einai upeuthunos gia auta
     * @return
     */
    public static HashMap<String,String> readAllBrokerTopicsFromConf(){
        HashMap<String,Integer> brokerIps= readAllBrokersFromConfToHashMap();
        HashMap<String,String> topics = new HashMap<>();
        try {
            File file = new File(PATH);
            Scanner line = new Scanner(file);
            boolean topicFound = false;
            while (line.hasNextLine()) {
                String data = line.nextLine();
                if (data.equals("Topics")){
                    topicFound = true;
                    continue;
                }
                if(topicFound) {
                    topics.put(data,saveToProperBroker(data,brokerIps));
                }
            }
            line.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return topics;
    }

    /**
     * methodos gia na apothikeusei to topic ston katallilo broker
     * @param topicName : to onoma tou topic
     * @param brokers   : HashMap me ip kai port gia kathe broker
     * @return          : gurnaei tin ip tou broker pou tha apothikeutei to topic
     */
    public static String saveToProperBroker(String topicName,HashMap<String, Integer> brokers){
        String hashedStr= topicToSHA1Hash(topicName);
        String strToReturn="";
        for(Map.Entry<String,Integer> broker: brokers.entrySet()){
            String hashedBroker= topicToSHA1Hash(broker.getKey()+ broker.getValue());
            if(hashedStr.compareTo(hashedBroker)<0){
                strToReturn=broker.getKey();
            }
        }
        return strToReturn;
    }

    public static Queue<Message> readConversationOfTopic(String fileName){

        Queue<Message> messages = new LinkedList<>();

        try {
            File file = new File(FOLDER_PATH + fileName + ".txt");
            Scanner line = new Scanner(file);
            while (line.hasNextLine()){
                String message = line.nextLine();
                String profileName = "", messageSend = "";
                if(message.charAt(0) == '#'){
                    int index = message.indexOf(":");
                    profileName = message.substring(1, index);
                    messageSend = message.substring(index+2, message.length());
                }

                System.out.println(profileName + "-send:" + messageSend);

                ProfileName name = new ProfileName(profileName);
                messages.add(new Message(messageSend, name));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public static byte[] loadFile(String path){
        try{
            File file = new File(FOLDER_PATH + path);
            FileInputStream fis = new FileInputStream(file);
            byte[] fileData = new byte[(int)file.length()];
            fis.read(fileData);
            return fileData;
        }catch (FileNotFoundException e){
            System.err.println("File not found!");
        }catch (IOException e){
            System.err.println("Error at loadFile() ");
        }
        return null;
    }

    /**
     * Splits a file's data into smaller parts
     * @param chunkSize the chunk size in bytes
     */
    public static List<byte[]> chunkifyFile(byte[] data, int chunkSize){
        List<byte[]> result = new ArrayList<>();
        int chunks = data.length / chunkSize;

        for (int i = 0; i < chunks ; i++) {
            byte[] current = new byte[chunkSize];
            for (int j = 0; j < chunkSize ; j++) {
                current[j] = data[j + i*chunkSize];
            }
            result.add(current);
        }

        //Add last chunk
        boolean isRemaining = data.length % chunkSize != 0;
        if (isRemaining){
            int remaining = data.length % chunkSize;
            int offset = chunks * chunkSize;

            byte[] current = new byte[remaining];
            for(int i = 0; i < remaining; ++i){
                current[i] = data[offset + i];
            }

            result.add(current);
        }

        return result;
    }
}
