package mq.com.chuohapps.utils.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nguyen.dang.tho on 9/1/2017.
 */

public final class CommonUtils {
    private CommonUtils() {
    }

    /**
     * Check email is valid or not
     *
     * @param email input email address
     * @return true if valid
     */
    public static boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
