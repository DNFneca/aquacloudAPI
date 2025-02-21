package cloud.aquacloud.aquacloudapi.function;

import java.security.MessageDigest;

public class Hash {
    public static String generateSHASHA3_512(String message) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA3-512");
        byte[] hash = md.digest(message.getBytes());
        return bytesToHex(hash);
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

}

