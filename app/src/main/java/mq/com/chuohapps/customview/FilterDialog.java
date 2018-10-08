package mq.com.chuohapps.customview;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mq.com.chuohapps.R;
import mq.com.chuohapps.ui.home.adapter.LineRfidAdapter;
import mq.com.chuohapps.utils.views.KeyboardUtils;


public class FilterDialog extends MyAlertDialog {

    public FilterDialog(Context context) {
        super(context);
    }

    @Override
    protected int provideLayout() {
        return R.layout.dialog_filter_vehicle;
    }

    @Override
    protected void setupViews() {
        setupLayout();
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
}
