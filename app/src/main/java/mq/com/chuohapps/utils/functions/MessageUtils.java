package mq.com.chuohapps.utils.functions;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;
import mq.com.chuohapps.R;
import mq.com.chuohapps.utils.views.ViewUtils;

/**
 * Created by nguyen.dang.tho on 9/1/2017.
 */

public final class MessageUtils {
    public static int ERROR_CODE = 0;
    public static int WARNING_CODE = 1;
    public static int SUCCESS_CODE = 2;
    public static int NO_INTERNET_CODE = 3;
    public static int ERROR_CONNECTION_CODE = 4;
    public static int ERROR_UNDEFINE_CODE = 5;
    private static Toast toasty;
    private static MediaPlayer mediaPlayer;

    private MessageUtils() {
    }

    public static void init(Context context) {
        Toasty.Config.getInstance()
                .setErrorColor(context.getResources().getColor(R.color.messageError))
                .setSuccessColor(context.getResources().getColor(R.color.messageSuccess))
                .setWarningColor(context.getResources().getColor(R.color.messageWarning))
                .setInfoColor(context.getResources().getColor(R.color.messageWarning))
                .setTextColor(context.getResources().getColor(R.color.messageText))
                .setTextSize(ViewUtils.dpToPx(16))
                .apply();
    }

    public static <M> void show(@NonNull Activity context, M message) {
        success(context, message);
    }

    public static <M> void show(@NonNull Activity context, M message, int code) {
        if (code == ERROR_CODE) error(context, message);
        else if (code == SUCCESS_CODE) success(context, message);
        else if (code == WARNING_CODE) warning(context, message);
        else if (code == NO_INTERNET_CODE)
            noInternet(context, context.getResources().getString(R.string.message_no_internet));
        else if (code == ERROR_CONNECTION_CODE)
            error(context, context.getResources().getString(R.string.message_connect_error));
        else
            error(context, context.getResources().getString(R.string.message_undefine_error));

    }

    private static <M> void errorConnection(Activity activity, M message) {
        createMessage(activity, activity.getResources().getString(R.string.message_error_connection), R.color.messageError);
        playSound(activity, R.raw.warning_audio_beep);
    }

    private static <M> void noInternet(Activity context, M message) {
        createMessage(context, context.getResources().getString(R.string.message_error_internet), R.color.messageWarning);
        playSound(context, R.raw.warning_audio_beep);
    }

    /**
     * @param context
     * @param message
     * @param <M>
     */
    private static <M> void success(@NonNull Activity context, M message) {
        createMessage(context, message, R.color.messageSuccess);
        playSound(context, R.raw.double_ping_audio);
    }

    /**
     * @param context
     * @param message
     * @param <M>
     */
    private static <M> void warning(@NonNull Activity context, M message) {
        createMessage(context, message, R.color.messageWarning);
        playSound(context, R.raw.warning_audio);
    }


    private static <M> void error(@NonNull Activity activity, M message) {
        createMessage(activity, message, R.color.messageError);
        playSound(activity, R.raw.error_audio);
    }

    private static <M> void createMessage(@NonNull Activity activity, M message, int colorResource) {
//        Alerter.create(activity)
//                .setTitle(String.valueOf(message))
//                .setBackgroundColorRes(colorResource)
//                // .setIcon(getIcon(colorResource))
//                .setDuration(AppConfigs.MESSAGE_SHOWING_DURATION)
//                .hideIcon()
//                .enableSwipeToDismiss()
//                .show();

        if (toasty != null) toasty.cancel();
//        if (colorResource == R.color.messageError) {
//            toasty = Toasty.error(activity, String.valueOf(message), String.valueOf(message).length() > 7 ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT, true);
//
//        } else if (colorResource == R.color.messageSuccess) {
//            toasty = Toasty.success(activity, String.valueOf(message), String.valueOf(message).length() > 7 ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT, true);
//        } else if (colorResource == R.color.messageWarning) {
//            toasty = Toasty.warning(activity, String.valueOf(message), String.valueOf(message).length() > 7 ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT, true);
//        }
        toasty = Toasty.custom(activity, String.valueOf(message), null,
                activity.getResources().getColor(colorResource), String.valueOf(message).length() > 7 ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT, false, true);
        toasty.setGravity(Gravity.TOP, 0, ViewUtils.dpToPx(8));
        toasty.show();
    }

    private static void playSound(Activity activity, int resId) {
        mediaPlayer = MediaPlayer.create(activity, resId);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.reset();
                mediaPlayer.release();
            }
        });
        mediaPlayer.start();
    }

    private static int getIcon(int colorResource) {
        int iconResource = 0;
//        if (colorResource == R.color.messageError) {
//            iconResource = R.mipmap.ic_error;
//        } else if (colorResource == R.color.messageWarning) {
//            iconResource = R.mipmap.ic_error;
//        } else if (colorResource == R.color.messageSuccess) {
//            iconResource = R.mipmap.ic_success;
//        }
        return iconResource;
    }

    public static void cancelAll(Activity activity) {
//        Alerter.hide();
//        if (activity != null)
//            Alerter.clearCurrent(activity);
        if (toasty != null) toasty.cancel();
        toasty = null;
    }

    public static void release() {
        if (toasty != null) {
            toasty.cancel();
            toasty = null;
        }
    }

    private static void playCustomSound(Context context) {
//        MediaPlayer mPlayer = MediaPlayer.create(context, R.raw.notification);
//        mPlayer.setAudioStreamType(STREAM_MUSIC);
//        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                mp.release();
//            }
//        });
//        mPlayer.start();

    }

    private static void playSystemNotificationSound(Context context) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class Dialog {
        private AlertDialog dialog;

        public static DialogBuilder with(Context context) {
            return new DialogBuilder(context);
        }

        void create(Context context,
                    @NonNull String title,
                    @NonNull String message,
                    @NonNull String buttonYesText,
                    final Runnable buttonYesAction,
                    @NonNull String buttonNoText,
                    final Runnable buttonNoAction,
                    final Runnable buttonBackAction,
                    boolean cancelOnTouchOutSide) {
            if (context == null) return;
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder.setTitle(title);
            dialogBuilder.setMessage(message);
            dialogBuilder.setPositiveButton(buttonYesText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (dialog != null) dialog.dismiss();
                    if (buttonYesAction != null)
                        new Handler().post(buttonYesAction);
                }
            });
            dialogBuilder.setNegativeButton(buttonNoText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (dialog != null) dialog.dismiss();
                    if (buttonNoAction != null)
                        new Handler().post(buttonNoAction);
                }
            });
            dialog = dialogBuilder.create();
            dialog.setCanceledOnTouchOutside(cancelOnTouchOutSide);
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (dialog != null)
                            dialog.dismiss();
                        if (buttonBackAction != null) new Handler().post(buttonBackAction);
                    }
                    return true;
                }
            });
        }

        public void show() {
            if (dialog != null) dialog.show();
        }

        public void hide() {
            if (dialog != null) dialog.dismiss();
        }

        public static class DialogBuilder {
            private Context context;
            private String title;
            private String message;
            private String buttonYesText;
            private String buttonNoText;
            private Runnable buttonYesOnClick;
            private Runnable buttonNoOnClick;
            private Runnable buttonBackOnClick;
            private boolean cancelOnTouchOutSide = true;

            DialogBuilder(Context context) {
                this.context = context;
            }

            public DialogBuilder title(String title) {
                this.title = title == null ? "" : title;
                return this;
            }

            public DialogBuilder message(String message) {
                this.message = message == null ? "" : message;
                return this;
            }

            public DialogBuilder buttonYesText(String buttonYesText) {
                this.buttonYesText = buttonYesText == null ? "Yes" : buttonYesText;
                return this;
            }

            public DialogBuilder buttonNoText(String buttonNoText) {
                this.buttonNoText = buttonNoText == null ? "No" : buttonNoText;
                return this;
            }

            public DialogBuilder buttonYesOnClick(Runnable buttonYesOnClick) {
                this.buttonYesOnClick = buttonYesOnClick;
                return this;
            }

            public DialogBuilder buttonNoOnClick(Runnable buttonNoOnClick) {
                this.buttonNoOnClick = buttonNoOnClick;
                return this;
            }

            public DialogBuilder buttonBackOnClick(Runnable buttonBackOnClick) {
                this.buttonBackOnClick = buttonBackOnClick;
                return this;
            }

            public DialogBuilder cancelOnTouchOutSide(boolean enable) {
                this.cancelOnTouchOutSide = enable;
                return this;
            }

            public Dialog create() {
                Dialog dialog = new Dialog();
                dialog.create(context,
                        title,
                        message,
                        buttonYesText,
                        buttonYesOnClick,
                        buttonNoText,
                        buttonNoOnClick,
                        buttonBackOnClick,
                        cancelOnTouchOutSide);
                context = null;
                return dialog;
            }
        }
    }

    public static class Notification {
        public static <Model> void show(Activity activity, Model data, final Runnable runOnClick) {
//            LayoutInflater inflater = activity.getLayoutInflater();
//            View layout = inflater.inflate(R.layout.x_in_app_notification, null, false);
//            ImageView imageContent = (ImageView) layout.findViewById(R.id.imageContent);
//            ImageView imageLogo = (ImageView) layout.findViewById(R.id.imageCompanyLogo);
//            TextView textTitle = (TextView) layout.findViewById(R.id.textTitle);
//            TextView textCompanyName = (TextView) layout.findViewById(R.id.textCompanyName);
//            layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onChangeStatus(View v) {
//                    new Handler().postDelayed(runOnClick, 0);
//                }
//            });
//            TextView textContent = (TextView) layout.findViewById(R.id.textContent);
//            ImageUtils.loadImageCircle(imageContent, data.getImageContentUrl());
//            ImageUtils.loadImageCircle(imageLogo, data.getImageCompanyUrl());
//            textTitle.setText(data.getTitle());
//            textCompanyName.setText(data.getCompanyName());
//            textContent.setText(data.getTextContent());
//            Toast toast = new Toast(activity);
//            toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 12);
//            toast.setDuration(Toast.LENGTH_LONG);
//            toast.setView(layout);
//            toast.show();
            playCustomSound(activity);
        }
    }
}
