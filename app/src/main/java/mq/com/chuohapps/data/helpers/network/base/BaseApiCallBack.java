package mq.com.chuohapps.data.helpers.network.base;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.TimeoutException;

import mq.com.chuohapps.utils.AppLogger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseApiCallBack<R> implements Callback<R> {


    /**
     * @param call
     * @param response
     */
    @Override
    public void onResponse(@NonNull Call<R> call, @NonNull Response<R> response) {
            if (response.body() != null) {
                handleSuccessResponse(response);
            } else {
                onReceiveResponseError(response);
            }
    }
    private void onReceiveResponseError(@NonNull Response<R> response) {

    }

    protected abstract void handleSuccessResponse(@NonNull Response<R> response);

    /**
     * @param call
     * @param throwable
     */
    @Override
    public void onFailure(@NonNull Call<R> call, @NonNull Throwable throwable) {
        AppLogger.error(this, throwable);
        if (throwable instanceof ConnectException) {
            onError(throwable, "Can't connect to server!");
        } else if (throwable instanceof TimeoutException) {
            onError(throwable, "No response from server!");
        } else if (throwable instanceof IOException) {
            onError(throwable, "Can't connect to internet!");
        } else {
            onError(throwable, throwable.getMessage());
        }
    }

    /**
     * @param response
     * @param message
     */
    public abstract void onSuccess(R response, String message);

    /**
     * @param throwable
     * @param message
     */
    public abstract void onError(Throwable throwable, String message);

}
