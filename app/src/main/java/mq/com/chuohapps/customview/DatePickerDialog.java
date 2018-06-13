package mq.com.chuohapps.customview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by nguyen.dang.tho on 2/28/2018.
 */

public class DatePickerDialog {
    android.app.DatePickerDialog datePickerDialog = null;
    private Listener listener = null;

    public DatePickerDialog(@NonNull Context context, Calendar calendar, String buttonNoText) {
        datePickerDialog = new android.app.DatePickerDialog(context, AlertDialog.THEME_HOLO_LIGHT, new android.app.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar setCalendar = Calendar.getInstance();
                setCalendar.set(year, month, dayOfMonth);
                if (listener != null) {
                    listener.onDateSet(setCalendar);
                    listener = null;
                }
                release();
            }
        },
                calendar == null ? Calendar.getInstance().get(Calendar.YEAR) : calendar.get(Calendar.YEAR),
                calendar == null ? Calendar.getInstance().get(Calendar.MONTH) : calendar.get(Calendar.MONTH),
                calendar == null ? Calendar.getInstance().get(Calendar.DAY_OF_MONTH) : calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, buttonNoText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                    if (listener != null) {
                        listener.onCancelClick();
                        listener = null;
                    }
                    release();

                }
            }
        });
        datePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (listener != null) {
                    listener.onDismiss();
                }
                release();
            }
        });
    }

    public void release() {
        try {
            if (datePickerDialog != null) {
                datePickerDialog.dismiss();
                datePickerDialog = null;
            }
        } catch (Exception ignored) {

        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void show() {
        if (datePickerDialog != null) {
            if (!datePickerDialog.isShowing())
                datePickerDialog.show();
        }
    }

    public interface Listener {
        void onDateSet(Calendar calendar);

        void onCancelClick();

        void onDismiss();
    }
}
