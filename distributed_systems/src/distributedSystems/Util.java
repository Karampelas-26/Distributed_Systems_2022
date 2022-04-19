package distributedSystems;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public final class Util {

    private static final String PATH = "src/distributedSystems/conf.txt";
    /**
     * Reading from the file conf.txt information about brokers
     * @return An Arraylist with Triplets<BrokerID, IP Address, Port>
     */
    public  static ArrayList<Triplet<Integer, String, Integer>> readAllBrokersFromConfToArrayListOfTtriplets() {

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
}
