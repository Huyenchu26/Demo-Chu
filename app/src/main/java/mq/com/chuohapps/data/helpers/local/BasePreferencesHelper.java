package mq.com.chuohapps.data.helpers.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;



import mq.com.chuohapps.AppConfigs;
import mq.com.chuohapps.utils.AppLogger;
import mq.com.chuohapps.utils.data.SharedPrefsUtils;


public abstract class BasePreferencesHelper {


    private SharedPreferences sharedPreferences;

    public BasePreferencesHelper(@NonNull Context context) {
        sharedPreferences = context.getSharedPreferences(AppConfigs.PREF_NAME, Context.MODE_PRIVATE);
    }

    protected SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public <Data> void saveWithKey(@NonNull String key, Data data) {
        SharedPrefsUtils.put(sharedPreferences, key, data);
    }

    public <Data> Data getWithKey(@NonNull String key, Class<Data> classType, Data defaultValue) {
        return SharedPrefsUtils.get(sharedPreferences, key, classType, defaultValue);
    }

    public void clear() {
        SharedPrefsUtils.clear(sharedPreferences);
    }


    public <R> void saveCache(R data, Class<?> classType) {
        save(classType.getCanonicalName(), data, new PrefsCallBack() {
            @Override
            public void onSuccess(Object response) {

            }
        });
    }


    public <R> void getCache(Class<R> classType, PrefsCallBack<R> callBack) {
        load(classType.getCanonicalName(), classType, null, callBack);
    }

    public <R> R getCache(Class<R> classType) {
        return SharedPrefsUtils.get(sharedPreferences, classType.getCanonicalName(), classType, null);
    }

    public <R> void deleteCache(Class<R> classType) {
        SharedPrefsUtils.delete(sharedPreferences, classType.getCanonicalName());
    }

    @SuppressWarnings("unchecked")
    protected <D> void save(final String key, D data, @NonNull final PrefsCallBack callBack) {
        new AsyncTask<D, Void, D>() {
            @Override
            protected D doInBackground(D... dataList) {
                try {
                    SharedPrefsUtils.put(sharedPreferences, key, dataList[0]);
                } catch (Exception e) {
                    AppLogger.error("PrefsHelper save error with key:" + key);
                    return null;
                }

                return dataList[0];
            }

            @Override
            protected void onPostExecute(D data) {
                super.onPostExecute(data);
                if (callBack != null)
                    callBack.onSuccess(data);
            }
        }.execute(data);
    }

    @SuppressWarnings("unchecked")
    protected <D> void load(final String key, final Class<D> classType, final D defaultValue, final PrefsCallBack<D> callBack) {
        new AsyncTask<Void, Void, D>() {
            @Override
            protected D doInBackground(Void... voids) {
                D data = null;
                try {
                    data = SharedPrefsUtils.get(sharedPreferences, key, classType, defaultValue);
                } catch (Exception e) {
                    AppLogger.error("PrefsHelper load error with key:" + key);
                }
                return data;
            }

            @Override
            protected void onPostExecute(D data) {
                super.onPostExecute(data);
                if (callBack != null)
                    callBack.onSuccess(data);
            }
        }.execute();
    }

}
