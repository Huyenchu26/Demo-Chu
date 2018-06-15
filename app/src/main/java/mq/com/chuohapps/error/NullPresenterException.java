package mq.com.chuohapps.error;

public class NullPresenterException extends IllegalArgumentException {
    public NullPresenterException() {
        super("Please add Presenter to PresenterProvider!");
    }
}
