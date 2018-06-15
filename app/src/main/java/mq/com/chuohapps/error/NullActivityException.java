package mq.com.chuohapps.error;

public class NullActivityException extends Exception {
    public NullActivityException() {
        super("Activity is Null!");
    }
}
