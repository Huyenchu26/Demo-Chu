package mq.com.chuohapps.ui.home.dialog;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import mq.com.chuohapps.R;
import mq.com.chuohapps.customview.DatePickerDialog;
import mq.com.chuohapps.customview.MyAlertDialog;
import mq.com.chuohapps.utils.data.DateUtils;
import mq.com.chuohapps.utils.views.KeyboardUtils;

public class DateDialog extends MyAlertDialog {

    @BindView(R.id.editStartTime)
    EditText editStartTime;
    @BindView(R.id.editEndTime)
    EditText editEndTime;
    @BindView(R.id.container)
    View container;


    private Calendar date = null;
    private OnChooseListener onChooseListener;

    public DateDialog(Context context) {
        super(context);
    }

    @Override
    protected int provideLayout() {
        return R.layout.dialog_select_time;
    }

    private int choose = 0;

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
        editEndTime.setFocusable(false);
        editStartTime.setOnClickListener(new mq.com.chuohapps.customview.OnClickListener() {
            @Override
            public void onDelayedClick(View v) {
                openDatePicker();
                choose = 1;
            }
        });
        editEndTime.setOnClickListener(new mq.com.chuohapps.customview.OnClickListener() {
            @Override
            public void onDelayedClick(View v) {
                openDatePicker();
                choose = 2;
            }
        });
    }


    public void setOnChooseListener(OnChooseListener onChooseListener) {
        this.onChooseListener = onChooseListener;
    }

    public void release() {
        super.release();
        onChooseListener = null;
        date = null;
    }

    @OnClick(R.id.buttonSearchOrder)
    public void buttonSearchClicked() {
        if (onChooseListener != null)
            try {
                onChooseListener.onDone(editStartTime.getText().toString(), editEndTime.getText().toString());
            } catch (NumberFormatException ignored) {
            }
        onChooseListener = null;
        dismiss();
    }

    private void openDatePicker() {
        DatePickerDialog datePickerDialog =
                new DatePickerDialog(getContext(), date, "Cancel");
        datePickerDialog.setListener(new DatePickerDialog.Listener() {
            @Override
            public void onDateSet(Calendar calendar) {
                date = calendar;
                if (choose == 1)
                    editStartTime.setText(DateUtils.dateToString(date.getTime()));
                else if (choose == 2)
                    editEndTime.setText(DateUtils.dateToString(date.getTime()));
                else return;
            }

            @Override
            public void onCancelClick() {
                editStartTime.setText(null);
                choose = 0;
            }

            @Override
            public void onDismiss() {
                release();
                choose = 0;
            }
        });
        datePickerDialog.show();
    }

    public interface OnChooseListener {
        void onDone(String startDate, String endDate);
    }
}
