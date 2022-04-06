package distributedSystems;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Objects;

import static java.util.Objects.hash;

public class UserNode extends Thread{


    private static String sha1Hash(String value){

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

    public void run() {

    }

    public static void main(String[] args) {
//        System.out.println(Math.abs(Objects.hash("192.168.1.100_5000")) % 300);
//        System.out.println(Math.abs(Objects.hash("192.168.1.101_5001")) % 300);
//        System.out.println(Math.abs(Objects.hash("192.168.1.102_5002")) % 300);
//        System.out.println(Math.abs(Objects.hash("katanemimena_systimata")) % 300);
//        System.out.println(Math.abs(Objects.hash("karampelas")) % 300);
//        System.out.println(Math.abs(Objects.hash("magklaras")) % 300);
//        System.out.println(Math.abs(Objects.hash("trolinos")) % 300);
//        System.out.println(Math.abs(Objects.hash("xristoulakis")) % 300);
//        System.out.println(Math.abs(Objects.hash("sifis-antonis")) % 300);
//        System.out.println(Math.abs(Objects.hash("sifis")) % 300);
//        System.out.println(Math.abs(Objects.hash("magteo")) % 300);
////        System.out.println(Objects.hashCode("192.168.1.103_5003") % 1000);
////        System.out.println(Objects.hashCode("192.168.1.104_5004") % 1000);
////        System.out.println(Objects.hashCode("192.168.1.105_5005") % 1000);
////        System.out.println(Objects.hashCode("192.168.1.106_5006") % 1000);
//
//        System.out.println(Math.abs((hash("antonis-george") + hash("192.168.1.100_5000")) %1000 ) +"====="+ Math.abs(hash("192.168.1.100_5000") % 1000));
//        System.out.println(Math.abs((hash("katanemimena_systimata") + hash("192.168.1.100_5000")) %1000 ) +"====="+ Math.abs(hash("192.168.1.101_5001") % 1000));
//        System.out.println(Math.abs((hash("antonis") + hash("192.168.1.100_5000")) %1000 ) +"====="+ Math.abs(hash("192.168.1.102_5002") % 1000));
//
//
//
//        System.out.println(Math.abs(hash(sha1Hash("antonnis"))) % 1000);
//        System.out.println(Math.abs(hash(sha1Hash("192.168.1.100_5000"))) % 1000);
//        System.out.println(Math.abs(hash(sha1Hash("192.168.1.101_5001"))) % 1000);
//        System.out.println(Math.abs(hash(sha1Hash("192.168.1.102_5002"))) % 1000);
//
//        String b1h = sha1Hash("192.168.1.100_5000");
//        String b2h = sha1Hash("192.168.1.101_5001");
//        String b3h = sha1Hash("192.168.1.102_5002");
//        String an = sha1Hash("katanemimsasdfe");
//
//        System.out.println(b1h + "       " + an);
//
//        if (an.compareTo(b1h) < -1){
//            System.out.println(1);
//        }else if (an.compareTo(b3h) < -1){
//            System.out.println(3);
//        }else if (an.compareTo(b2h) < -1){
//            System.out.println(2);
//        }
//        else {
//            System.out.println(an.compareTo(b1h));
//            System.out.println(an.compareTo(b2h));
//            System.out.println(an.compareTo(b3h));
//        }
    }

}
