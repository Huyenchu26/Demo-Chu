package mq.com.chuohapps.data.helpers.network.base;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import mq.com.chuohapps.AppConfigs;
import mq.com.chuohapps.data.DataCallBack;
import mq.com.chuohapps.data.helpers.network.ApiCallBack;
import mq.com.chuohapps.data.helpers.network.client.ApiClient;
import mq.com.chuohapps.utils.AppLogger;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public abstract class BaseApiHelper {

    private OkHttpClient client;
    private ApiClient apiClient;
    private List<Call> callList = new ArrayList<>();

    public BaseApiHelper(OkHttpClient client) {
        this.client = client;
    }

    protected ApiClient getClient() {
        if (apiClient == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(AppConfigs.HTTP_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(AppConfigs.HTTP_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(AppConfigs.HTTP_TIMEOUT, TimeUnit.SECONDS)
                    .build();
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(AppConfigs.HOST)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson));
            if (AppConfigs.isEnableLog) builder = builder.client(client);
            Retrofit retrofit = builder.build();
            apiClient = retrofit.create(ApiClient.class);
        }
        return apiClient;
    }

    public void resetClient() {
        apiClient = null;
    }

    @SuppressWarnings("unchecked")
    protected <R extends BaseResponse> void processRequest(@NonNull Call call, DataCallBack<R> dataCallBack) {
        call.enqueue(handle(dataCallBack));
        // callList.add(call);
    }

    public void cancelAllRequests() {
        if (client != null) client.dispatcher().cancelAll();
    }


    protected <R> ApiCallBack<R> handle(final DataCallBack<R> dataCallBack) {
        AppLogger.error("Api callback");
        return new ApiCallBack<R>() {
            @Override
            public void onSuccess(R response, String message) {
                if (dataCallBack != null)
                    dataCallBack.onSuccess(response, message);
                callList.clear();
                AppLogger.error("Api callback success");
            }

            @Override
            public void onError(Throwable throwable, String message) {
                if (dataCallBack == null) return;
                dataCallBack.onError(throwable, message);
                callList.clear();
                AppLogger.error("Api callback error");
            }
        };

    }

}
