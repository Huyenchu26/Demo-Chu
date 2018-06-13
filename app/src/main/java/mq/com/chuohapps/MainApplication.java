package mq.com.chuohapps;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.facebook.stetho.Stetho;

import mq.com.chuohapps.data.DataManager;
import mq.com.chuohapps.di.DataProvider;
import mq.com.chuohapps.utils.functions.MessageUtils;


public class MainApplication extends Application {
    // public static RefWatcher watcher;
    private static DataManager dataManager;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DataProvider.setContext(getApplicationContext());
        MessageUtils.init(getApplicationContext());
        setupDebugTools();
    }

    private void setupDebugTools() {
        if (AppConfigs.isEnableLog)
            Stetho.initializeWithDefaults(this);
        //  watcher = LeakCanary.install(this);
    }
}
