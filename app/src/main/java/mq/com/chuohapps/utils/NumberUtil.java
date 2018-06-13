package mq.com.chuohapps.utils;

import java.math.BigInteger;

public class NumberUtil {
    public static long hexToDecimal(String hexa) {
        int decimel = 0;
        decimel = Integer.parseInt(hexa, 16);
        return decimel;
    }

    public static String hexToBin(String s) {
        return new BigInteger(s, 16).toString(2);
    }

    public static boolean checkRFID(String string){
        if (string.length() != 8) return false;
        long str12 = hexToDecimal(string.substring(0, 2));
        long str34 = hexToDecimal(string.substring(2, 4));
        long str56 = hexToDecimal(string.substring(4, 6));
        long x12 = hexToDecimal(string.substring(6, 8));
        if (x12 == ((str12 ^ str34) ^ str56)) {
            return true;
        } else {
            return false;
        }
    }

}

