package mq.com.chuohapps.error;

/**
 * Created by nguyen.dang.tho on 9/6/2017.
 */

public class NullPresenterException extends IllegalArgumentException {
    public NullPresenterException() {
        super("Please add Presenter to PresenterProvider!");
    }
}
