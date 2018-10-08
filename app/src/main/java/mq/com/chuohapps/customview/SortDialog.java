package mq.com.chuohapps.customview;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mq.com.chuohapps.R;
import mq.com.chuohapps.ui.home.adapter.LineRfidAdapter;
import mq.com.chuohapps.ui.home.dialog.DateDialog;
import mq.com.chuohapps.utils.views.KeyboardUtils;


public class SortDialog extends MyAlertDialog {

    @BindView(R.id.optionImei)
    TextView optionImei;
    @BindView(R.id.optionSize)
    TextView optionSize;
    @BindView(R.id.optionDate)
    TextView optionDate;

    private int option = 0;
    private OnChooseListener onChooseListener;

    public SortDialog(Context context, int option) {
        super(context);
        this.option = option;
    }

    @Override
    protected int provideLayout() {
        return R.layout.dialog_filter_vehicle;
    }

    @Override
    protected void setupViews() {
        setupLayout();
        switch (option) {
            case 0: optionImei.setTextColor(getContext().getResources().getColor(R.color.colorFacebook));
//                optionDate.setTextColor(getContext().getResources().getColor(R.color.colorTextPrimary));
//                optionSize.setTextColor(getContext().getResources().getColor(R.color.colorTextPrimary));
                break;
            case 1: optionDate.setTextColor(getContext().getResources().getColor(R.color.colorFacebook));
                break;
            case 2: optionSize.setTextColor(getContext().getResources().getColor(R.color.colorFacebook));
                break;
        }
    }

    public void release() {
        super.release();
    }

    private void setupLayout() {
        if (getWindow() != null)
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        try {
            getWindow().setLayout(
                    getContext().getResources().getDisplayMetrics().widthPixels / 100 * 30,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            getWindow().getAttributes().gravity = Gravity.TOP | Gravity.RIGHT;
        } catch (Exception ignored) {

        }
    }

    @OnClick(R.id.optionImei)
    public void sortByImei() {
        if (onChooseListener != null) {
            option = 0;
            onChooseListener.onDone(option);
        }
        onChooseListener = null;
        dismiss();
    }

    @OnClick(R.id.optionDate)
    public void sortByDate() {
        if (onChooseListener != null) {
            option = 1;
            onChooseListener.onDone(option);
        }
        onChooseListener = null;
        dismiss();
    }

    @OnClick(R.id.optionSize)
    public void sortBySize() {
        if (onChooseListener != null) {
            option = 2;
            onChooseListener.onDone(option);
        }
        onChooseListener = null;
        dismiss();
    }

    public interface OnChooseListener {
        void onDone(int option);
    }

    public void setOnChooseListener(OnChooseListener onChooseListener) {
        this.onChooseListener = onChooseListener;
    }
}
