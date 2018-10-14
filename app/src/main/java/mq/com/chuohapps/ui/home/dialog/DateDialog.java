package mq.com.chuohapps.ui.home.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.lang.reflect.Field;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import mq.com.chuohapps.R;
import mq.com.chuohapps.customview.DatePickerDialog;
import mq.com.chuohapps.customview.MyAlertDialog;
import mq.com.chuohapps.utils.AppLogger;
import mq.com.chuohapps.utils.data.DateUtils;
import mq.com.chuohapps.utils.functions.MessageUtils;
import mq.com.chuohapps.utils.views.KeyboardUtils;

public class DateDialog extends MyAlertDialog {

    @BindView(R.id.editStartTime)
    EditText editStartTime;
    @BindView(R.id.container)
    View container;

    private OnChooseListener onChooseListener;

    public DateDialog(Context context) {
        super(context);
    }

    @Override
    protected int provideLayout() {
        return R.layout.dialog_select_time;
    }

    @Override
    protected void setupViews() {
        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                KeyboardUtils.hideKeyboard(view.getContext(), view);
                return false;
            }
        });
        editStartTime.setFocusable(false);
        editStartTime.setOnClickListener(new mq.com.chuohapps.customview.OnClickListener() {
            @Override
            public void onDelayedClick(View v) {
                openDatePicker();
            }
        });
    }


    public void setOnChooseListener(OnChooseListener onChooseListener) {
        this.onChooseListener = onChooseListener;
    }

    public void release() {
        super.release();
        onChooseListener = null;
    }

    @OnClick(R.id.buttonSearchOrder)
    public void buttonSearchClicked() {
        if (onChooseListener != null)
            try {
                String startDate = editStartTime.getText().toString().trim();
                if (startDate.length() > 0)
                    onChooseListener.onDone(startDate);
                else return;
            } catch (NumberFormatException ignored) {
            }
        onChooseListener = null;
        dismiss();
    }

    private void openDatePicker() {
        DatePickerDialog datePickerDialog =
                new DatePickerDialog(getContext(), Calendar.getInstance(), "Cancel");
        datePickerDialog.setListener(new DatePickerDialog.Listener() {
            @Override
            public void onDateSet(Calendar calendar) {
                editStartTime.setText(DateUtils.dateToStringShort(calendar.getTime()));
                return;
            }

            @Override
            public void onCancelClick() {
                editStartTime.setText(null);
            }

            @Override
            public void onDismiss() {
                release();
            }
        });
//        datePickerDialog.setMaxDate();
//        try {
//            Field[] datePickerDialogFields = datePickerDialog.getClass().getDeclaredFields();
//            for (Field datePickerDialogField : datePickerDialogFields) {
//                if (datePickerDialogField.getName().equals("mDatePicker")) {
//
//                    datePickerDialogField.setAccessible(true);
//                    DatePicker datePicker = (DatePicker) datePickerDialogField
//                            .get(datePickerDialog);
//                    Field datePickerFields[] = datePickerDialogField.getType()
//                            .getDeclaredFields();
//                    for (Field datePickerField : datePickerFields) {
//                        if ("mYearPicker".equals(datePickerField.getName())
//                                || "mYearSpinner".equals(datePickerField.getName())
//                                || "mMonthPicker".equals(datePickerField.getName())
//                                || "mMonthSpinner".equals(datePickerField.getName())) {
//                            datePickerField.setAccessible(true);
//                            Object dayPicker = new Object();
//                            dayPicker = datePickerField.get(datePicker);
//                            ((View) dayPicker).setVisibility(View.GONE);
//                        }
//                    }
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        datePickerDialog.show();
    }

    public interface OnChooseListener {
        void onDone(String startDate);
    }
}
