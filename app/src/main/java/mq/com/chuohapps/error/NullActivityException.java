package mq.com.chuohapps.error;

/**
 * Created by nguyen.dang.tho on 9/1/2017.
 */

public class NullActivityException extends Exception {
    public NullActivityException() {
        super("Activity is Null!");
    }
}
