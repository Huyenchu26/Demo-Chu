package mq.com.chuohapps.error;

/**
 * Created by nguyen.dang.tho on 9/6/2017.
 */

public class NullViewException extends Exception {
    public NullViewException() {
        super("View is gone!");
    }
}
