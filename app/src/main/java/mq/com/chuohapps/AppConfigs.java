package mq.com.chuohapps;


public final class AppConfigs {
    public static final long SPLASH_SHOWING_DURATION = 1000;// in mili-second
    public static final String IMAGE_TYPE = ".png";
    public static final String PREF_NAME = "my_pref";
    /**
     * User for permission at runtime.
     */
    public static final int REQUEST_CODE_PERMISSION = 999;
    public static final String EMPTY = "";
    public static final String SPACE = " ";
    public static final String NULL_STRING = "[ --- ]";

    public static final String LOG_TAG = "HC-Tag";

    public static final String HOST_MQ = "http://192.168.6.34:22222/api/";
    public static final String HOST = "http://192.168.47.103:22222/api/";
    public static final int HTTP_TIMEOUT = 20;//in second
    public static boolean isEnableLog = true;

    private AppConfigs() {
    }

}
