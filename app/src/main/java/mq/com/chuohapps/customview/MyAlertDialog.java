package mq.com.chuohapps.customview;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import butterknife.ButterKnife;

/**
 * Created by nguyen.dang.tho on 2/28/2018.
 */

public abstract class MyAlertDialog extends AlertDialog {
    private OnBackPressListener backPressListener;

    protected MyAlertDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(provideLayout());
        ButterKnife.bind(this);
        setupViews();
    }

    protected abstract int provideLayout();

    protected abstract void setupViews();

    @Override
    protected void onStart() {
        super.onStart();
        setupLayout();
    }

    private void setupLayout() {
        if (getWindow() != null)
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        try {
            getWindow().setLayout(
                    getContext().getResources().getDisplayMetrics().widthPixels / 100 * 85,
                    WindowManager.LayoutParams.WRAP_CONTENT);

        } catch (Exception ignored) {

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (backPressListener != null)
            backPressListener.onBackPress();
    }

    protected void release() {
        backPressListener = null;
    }

    public void setBackPressListener(OnBackPressListener backPressListener) {
        this.backPressListener = backPressListener;
    }

    public interface OnBackPressListener {
        void onBackPress();
    }
}
