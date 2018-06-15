package mq.com.chuohapps.error;

public class NullViewException extends Exception {
    public NullViewException() {
        super("View is gone!");
    }
}
