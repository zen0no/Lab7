package utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashGenerator {


    public static String hash(String obj){
        MessageDigest algorithm;
        try
        {
            algorithm = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException();
        }
        byte[] encodedHash = algorithm.digest(obj.getBytes(StandardCharsets.UTF_8));

        return bytesToHex(encodedHash);
    }

    private static String bytesToHex(byte[] hash){
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++){
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1){
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }
}
