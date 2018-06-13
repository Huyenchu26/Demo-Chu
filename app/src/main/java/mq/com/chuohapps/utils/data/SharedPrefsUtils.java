package mq.com.chuohapps.utils.data;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import mq.com.chuohapps.AppConfigs;

/**
 * Created by nguyen.dang.tho on 9/1/2017.
 */

public final class SharedPrefsUtils {

    private SharedPrefsUtils() {
    }

    /**
     * @param mSharedPreferences
     * @param key
     * @param anonymousClass
     * @param defaultValue
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(@NonNull SharedPreferences mSharedPreferences,
                            @NonNull String key,
                            @NonNull Class<T> anonymousClass,
                            @NonNull T defaultValue) {
        if (anonymousClass == String.class) {
            return (T) mSharedPreferences.getString(key, (String) defaultValue);
        } else if (anonymousClass == Boolean.class) {
            return (T) Boolean.valueOf(mSharedPreferences.getBoolean(key, (Boolean) defaultValue));
        } else if (anonymousClass == Float.class) {
            return (T) Float.valueOf(mSharedPreferences.getFloat(key, (Float) defaultValue));
        } else if (anonymousClass == Integer.class) {
            return (T) Integer.valueOf(mSharedPreferences.getInt(key, (Integer) defaultValue));
        } else if (anonymousClass == Long.class) {
            return (T) Long.valueOf(mSharedPreferences.getLong(key, (Long) defaultValue));
        } else {
            return new Gson().fromJson(mSharedPreferences.getString(key, AppConfigs.EMPTY), anonymousClass);
        }
    }

    /**
     * @param mSharedPreferences
     * @param key
     * @param data
     * @param <T>
     */
    public static <T> void put(@NonNull SharedPreferences mSharedPreferences,
                               @NonNull String key,
                               @NonNull T data) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if (data instanceof String) {
            editor.putString(key, (String) data);
        } else if (data instanceof Boolean) {
            editor.putBoolean(key, (Boolean) data);
        } else if (data instanceof Float) {
            editor.putFloat(key, (Float) data);
        } else if (data instanceof Integer) {
            editor.putInt(key, (Integer) data);
        } else if (data instanceof Long) {
            editor.putLong(key, (Long) data);
        } else {
            editor.putString(key, new Gson().toJson(data));
        }
        editor.apply();
    }

    public static void delete(@NonNull SharedPreferences mSharedPreferences,
                              @NonNull String key) {

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public static void clear(@NonNull SharedPreferences mSharedPreferences) {
        mSharedPreferences.edit().clear().apply();
    }
}
