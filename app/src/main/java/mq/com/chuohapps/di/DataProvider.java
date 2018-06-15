package mq.com.chuohapps.di;

import android.annotation.SuppressLint;
import android.content.Context;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import mq.com.chuohapps.AppConfigs;
import mq.com.chuohapps.data.AppDataManager;
import mq.com.chuohapps.data.DataManager;
import mq.com.chuohapps.data.helpers.local.PreferencesHelper;
import mq.com.chuohapps.data.helpers.network.ApiHelper;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class DataProvider {
    @SuppressLint("StaticFieldLeak")
    private static Context context = null;
    private static DataManager dataManager = null;

    private DataProvider() {
    }

    public static void setContext(Context context) {
        DataProvider.context = context;
    }

    public static void release() {
        context = null;
    }

    public static DataManager provideDataManager() {
        if (dataManager == null) {
            dataManager = new AppDataManager(
                    provideApi(),
                    providePrefs());
        }
        return dataManager;
    }

    private static PreferencesHelper providePrefs() {
        return new PreferencesHelper(context);
    }

    private static ApiHelper provideApi() {
        return new ApiHelper(provideApiClient());
    }

    private static OkHttpClient provideApiClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .connectTimeout(AppConfigs.HTTP_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(AppConfigs.HTTP_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(AppConfigs.HTTP_TIMEOUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(new StethoInterceptor())
                .addInterceptor(interceptor)
                .build();
    }
}
