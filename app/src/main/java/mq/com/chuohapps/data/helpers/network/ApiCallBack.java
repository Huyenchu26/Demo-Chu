package mq.com.chuohapps.data.helpers.network;

import android.support.annotation.NonNull;

import mq.com.chuohapps.data.helpers.network.base.BaseApiCallBack;
import retrofit2.Response;


public abstract class ApiCallBack<R> extends BaseApiCallBack<R> {

    @Override
    public void handleSuccessResponse(@NonNull Response<R> response) {
        try {
            if (response.body() != null)
                onSuccess(response.body(), "Success!!!");
            else
                onError(null, response.message());
        } catch (Exception ignored) {
            onError(null, response.message());
        }
    }

}
