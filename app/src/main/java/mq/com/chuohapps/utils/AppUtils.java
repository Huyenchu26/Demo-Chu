package mq.com.chuohapps.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.view.View;
import android.view.WindowManager;

import mq.com.chuohapps.AppConfigs;
import mq.com.chuohapps.BuildConfig;
import mq.com.chuohapps.R;
import mq.com.chuohapps.utils.functions.MessageUtils;


/**
 * Created by nguyen.dang.tho on 9/1/2017.
 */

public final class AppUtils {
    private AppUtils() {
    }

    /**
     * @param context activity
     */
    public static void openPlayStoreForApp(Context context) {
        final String appPackageName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(context
                            .getResources()
                            .getString(R.string.app_market_link) + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(context
                            .getResources()
                            .getString(R.string.app_google_play_store_link) + appPackageName)));
        }
    }

    /**
     * @param activity    .
     * @param permission  .
     * @param requestCode .
     * @return true if granted
     */
    public static boolean requestPermission(final Activity activity, String permission, int requestCode) {
        return requestPermissions(activity, new String[]{permission}, requestCode);
    }

    public static boolean requestPermissions(final Activity activity, String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!checkPermissionGranted(activity, permissions)) {
                if (shouldShowPermissionSetting(activity, permissions)) {
                    openSettingDialog(activity);
                } else {
                    ActivityCompat.requestPermissions(activity, permissions, requestCode);
                }
                return false;
            }
        }
        return true;
    }

    private static boolean checkPermissionGranted(Activity activity, String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    private static boolean shouldShowPermissionSetting(Activity activity, String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission))
                return true;
        }
        return false;
    }

    private static void openSettingDialog(final Activity activity) {
        if (activity == null) return;
        MessageUtils.Dialog.with(activity)
                .title(activity.getResources().getString(R.string.permission_dialog_title))
                .message(activity.getResources().getString(R.string.permission_dialog_message))
                .buttonYesText(activity.getResources().getString(R.string.permission_dialog_button_yes))
                .buttonYesOnClick(new Runnable() {
                    @Override
                    public void run() {
                        goToAppSetting(activity);
                    }
                })
                .buttonNoOnClick(new Runnable() {
                    @Override
                    public void run() {
                        openSettingDialog(activity);
                    }
                })
                .buttonNoText(activity.getResources().getString(R.string.permission_dialog_button_no))
                .create()
                .show();
    }

    private static void goToAppSetting(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivity(intent);
    }

    /**
     * Go to home of phone like press home button in navigation bar
     *
     * @param activity what activity want to ...
     */
    public static void goHome(@NonNull Activity activity) {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(startMain);
    }

    public static void runOutOfApp(@NonNull Activity activity) {
        activity.moveTaskToBack(true);
    }

//    public static String getIMEI(Activity activity) {
//        TelephonyManager telephonyManager = (TelephonyManager) activity
//                .getSystemService(Context.TELEPHONY_SERVICE);
//        return telephonyManager.getDeviceId();
//    }

    public static String getDeviceUniqueID(Activity activity) {
        String device_unique_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return device_unique_id;
    }

    public static String getAppVersion(Context context) {
        String version;
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            version = BuildConfig.VERSION_NAME;
        }
        return version;
    }

    public static String getOSName() {
        String osName = "Android";
        try {
            osName += "-" + Build.VERSION_CODES.class.getFields()[Build.VERSION.SDK_INT + 1].getName();
        } catch (Exception ignored) {
        }
        return osName;
    }

    public static void openHttpLink(Context context, String uri) {
        if (uri == null || uri.equals(AppConfigs.EMPTY)) return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uri));
        context.startActivity(intent);
    }

    public static void dialPhoneNumber(Activity activity, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + Uri.encode(phoneNumber)));
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
        //        Intent intent = new Intent(Intent.ACTION_DIAL);
//                intent.add(Uri.fromParts("tel","*100*"+moneyKey.replace(" ","")+"#", "#"));
//                startActivity(intent);
    }

    public static void copyToClipBoard(Activity activity, String textToCopy) {
        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(activity.getResources().getString(R.string.app_name), textToCopy);
        clipboard.setPrimaryClip(clip);
    }

    public static void sendSms(String phone, String sms) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phone, null, sms, null, null);
        //                            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//                            sendIntent.putExtra("+84923317403", "Point Card - Đổi tiền:Mã xác nhận của bạn là "+code);
//                            sendIntent.setType("vnd.android-dir/mms-sms");
//                            startActivity(sendIntent);
    }

    public static void makeScreenOn(Activity activity, PowerManager.WakeLock mWakeLock) {
        if (activity == null) return;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (mWakeLock == null) {
            PowerManager pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "WakeLockTag");
        }
        mWakeLock.acquire();
    }

    public static void makeScreenOff(Activity activity, PowerManager.WakeLock mWakeLock) {
        if (activity == null) return;
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (mWakeLock == null) return;
        mWakeLock.release();
    }

    public static void hideNavigationBar(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public static void showNavigationBar(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
