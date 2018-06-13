package mq.com.chuohapps.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class GetRFID {
    private static boolean isTag(char s1, char s2, char s3) {
        if (s1 == s2 && s2 == s3 && s3 == 'f') {
            return true;
        } else {
            return false;
        }
    }

    private static int chuyenInt(char s) {
        if (s <= 57 && s >= 30) {
            s -= 48;
        } else {
            s -= 87;
        }

        return s;
    }

    private static boolean isCheckSum(char s1, char s2, char s3, char s4, char s5, char s6, char x1, char x2) {
        long s12 = chuyenInt(s1) * 16 + chuyenInt(s2);
        long s34 = chuyenInt(s3) * 16 + chuyenInt(s4);
        long s56 = chuyenInt(s5) * 16 + chuyenInt(s6);
        long x12 = chuyenInt(x1) * 16 + chuyenInt(x2);
        if (x12 == ((s12 ^ s34) ^ s56)) {
            return true;
        } else {
            return false;
        }
    }

    public static List<String> xuLy(String str) {
        List<String> rfid = new ArrayList<>();

        int size = str.length();
        for (int i = 2; i < size; i++) {
            if (isTag(str.charAt(i - 2), str.charAt(i - 1), str.charAt(i))) {
                while (i < size && (str.charAt(i) == 'f')) {
                    i++;
                }

                int end = i;
                while (end < size && (!isTag(str.charAt(end - 2), str.charAt(end - 1), str.charAt(end)))) {
                    end++;
                }

                if (isCheckSum(str.charAt(end - 8), str.charAt(end - 7), str.charAt(end - 6), str.charAt(end - 5), str.charAt(end - 4), str.charAt(end - 3), str.charAt(end - 2), str.charAt(end - 1))) {
                    end += 2;
                } else {
                    if (isCheckSum(str.charAt(end - 9), str.charAt(end - 8), str.charAt(end - 7), str.charAt(end - 6), str.charAt(end - 5), str.charAt(end - 4), str.charAt(end - 3), str.charAt(end - 2))) {
                        end++;
                    }
                }

                if (!(end == size)) {
                    end -= 3;
                } else {
                    end = size - 1;
                }

                rfid.add(str.substring(end - 7, end - 1));
                i = end;
            }

        }
        Log.e("HuyenChu", rfid.toString());
        return rfid;
    }

    public static List<String> getRFID(String string) {
        List<String> rfid = new ArrayList<>();
        for (int i = 0; i < (string.length()-8); i++) {
            String str = string.substring(i, i + 8);
            if (NumberUtil.checkRFID(str) && !str.contains("ffff")){
                rfid.add(String.valueOf(NumberUtil.hexToDecimal(str.substring(0, 6))));
            }
        }
        return rfid;
    }

}
