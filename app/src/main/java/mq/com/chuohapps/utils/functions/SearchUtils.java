package mq.com.chuohapps.utils.functions;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Created by nguyen.dang.tho on 9/14/2017.
 */

public final class SearchUtils {
    public static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replace("đ", "d").replace("Đ", "D");
    }
}
