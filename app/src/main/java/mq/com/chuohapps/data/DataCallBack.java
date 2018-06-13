package mq.com.chuohapps.data;

public abstract class DataCallBack<R> {
    /**
     * @param response
     * @param message
     */
    public abstract void onSuccess(R response, String message);

    /**
     * @param throwable ...
     * @param message   Error message!
     */
    public void onError(Throwable throwable, String message) {

    }

    /**
     * Logout when token invalid
     */
    public void onNeedLogout() {

    }
}
