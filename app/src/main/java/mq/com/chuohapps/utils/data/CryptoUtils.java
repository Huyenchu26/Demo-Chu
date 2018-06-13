package mq.com.chuohapps.utils.data;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by nguyen.dang.tho on 2/21/2018.
 */

public class CryptoUtils {
    private CryptoUtils() {
    }

    public static String getMd5(String... elements) {
        try {
            StringBuilder raw = new StringBuilder();
            for (String element : elements) raw.append(element);

            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(raw.toString().getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest)
                hexString.append(Integer.toHexString(0xFF & aMessageDigest));

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
