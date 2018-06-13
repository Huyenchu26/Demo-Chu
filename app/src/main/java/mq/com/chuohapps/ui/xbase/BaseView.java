package mq.com.chuohapps.ui.xbase;

import android.widget.EditText;

/**
 * Created by nguyen.dang.tho on 10/23/2017.
 */

public interface BaseView {
    <M> void showMessage(M message);

    <M> void showMessageMain(M message);

    <M> void showMessage(M message, int code);

    <M> void showMessageMain(M message, int code);

    /**
     * Show loading when doing long running
     */
    void showLoading();

    /**
     * Hide loading when done any action call showLoading()
     */
    void hideLoading();

    /**
     * Hide the soft keyboard if it is showing
     */
    void hideKeyboard();

    /**
     * Show the soft keyboard
     */
    void showKeyboard(EditText editText);

    /**
     * Check if phone connected to internet
     *
     * @return true if online
     */
    boolean isNetworkConnected();

    void restart();
}
