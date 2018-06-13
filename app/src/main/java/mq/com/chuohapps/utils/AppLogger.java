package mq.com.chuohapps.utils;

import android.util.Log;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import mq.com.chuohapps.AppConfigs;
import mq.com.chuohapps.utils.data.DataFormatUtils;
import okhttp3.OkHttpClient;

/**
 * Created by nguyen.dang.tho on 9/1/2017.
 */

public final class AppLogger {
    private static final int TYPE_DEBUG = 0;
    private static final int TYPE_ERROR = 1;
    private static final int TYPE_VERBOSE = 2;
    private static final int TYPE_INFO = 3;
    private static boolean isLogEnabled = AppConfigs.isEnableLog;

    private AppLogger() {
    }


    public static <T> void error(T message) {
        showLog(TYPE_ERROR, AppConfigs.LOG_TAG, "", message);
    }

    public static <T> void error(Object object, T message) {
        error(object.getClass().getSimpleName(), message);
    }

    public static <T> void error(String prefix, T message) {
        showLog(TYPE_ERROR, AppConfigs.LOG_TAG, prefix, message);
    }

    public static <T> void debug(T message) {
        showLog(TYPE_DEBUG, AppConfigs.LOG_TAG, "", message);
    }

    public static <T> void debug(Object object, T message) {
        debug(object.getClass().getSimpleName(), message);
    }

    public static <T> void debug(String prefix, T message) {
        showLog(TYPE_DEBUG, AppConfigs.LOG_TAG, prefix, message);
    }

    public static <T> void network(T message) {
        showLog(TYPE_ERROR, "MY_CLIENT", "", message);
    }

    public static <T> void lifecycle(T message) {
        showLog(TYPE_INFO, "LIFECYCLE", "", message);
    }

    public static <T> void memory(T message) {
        showLog(TYPE_INFO, "MY_MEMORY", "", message);
    }

    public static OkHttpClient getLoggingClientWithStetho() {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }

    private static <T> void showLog(int type, String tag, String prefix, T message) {
        if (!isLogEnabled) return;
        String prefixString = "";
        if (prefix != null && prefix.length() > 0) prefixString = "[" + prefix + "] ";
        String messageString = getMessage(message);
        switch (type) {
            case TYPE_DEBUG:
                Log.d(tag, prefixString + messageString);
                break;
            case TYPE_ERROR:
                Log.e(tag, prefixString + messageString);
                break;
            case TYPE_VERBOSE:
                Log.e(tag, prefixString + messageString);
                break;
            case TYPE_INFO:
                Log.e(tag, prefixString + messageString);
                break;
            default:
                Log.e(tag, prefixString + messageString);
                break;
        }

    }

    private static <T> String getMessage(T message) {
        if (message == null) return "NULL!!!";
        String messageString = "Unknown!";
        if (message instanceof Exception) {
            messageString = "[" + ((Exception) message).getClass().getSimpleName() + "]"
                    + ((Exception) message).getMessage();
        } else if (message instanceof Throwable) {
            messageString = "[" + ((Throwable) message).getClass().getSimpleName() + "]"
                    + ((Throwable) message).getMessage();
        } else {
            if (message instanceof String
                    || message instanceof Boolean
                    || message instanceof Float
                    || message instanceof Integer
                    || message instanceof Long
                    ) {
                messageString = String.valueOf(message);
            } else {
                try {
                    messageString = DataFormatUtils.getJsonPretty(message);
                } catch (Exception e) {
                    messageString = message.toString();
                }
            }
        }
        return messageString;
    }
}
