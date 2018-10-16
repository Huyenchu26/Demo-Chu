package mq.com.chuohapps.ui.home.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mq.com.chuohapps.AppConfigs;
import mq.com.chuohapps.R;
import mq.com.chuohapps.utils.functions.MessageUtils;
import mq.com.chuohapps.utils.views.KeyboardUtils;

public class UpdateNumberCarDialog extends AlertDialog {

    @BindView(R.id.textNumberCar)
    TextView textUpdate;
    @BindView(R.id.container)
    View container;
    private OnBackPressListener backPressListener;
    private OnChooseListener onChooseListener;

    private String oldNumber;

    public UpdateNumberCarDialog(Context context, String number) {
        super(context);
        this.oldNumber = number;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update_data);
        ButterKnife.bind(this);

        textUpdate.setText(oldNumber);
        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                KeyboardUtils.hideKeyboard(view.getContext(), view);
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getWindow() != null)
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    public void setBackPressListener(OnBackPressListener backPressListener) {
        this.backPressListener = backPressListener;
    }

    public void setOnChooseListener(OnChooseListener onChooseListener) {
        this.onChooseListener = onChooseListener;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (backPressListener != null)
            backPressListener.onBackPress();
    }
    String str = "";
    @OnClick(R.id.buttonUpdate)
    public void onButtonUpdateClicked() {
        if (onChooseListener != null) {
            str = textUpdate.getText().toString().trim();
            if (str.length() > 0)
                onChooseListener.onDone(str);
        }
        onChooseListener = null;
        dismiss();
    }

    public void release() {
        onChooseListener = null;
        backPressListener = null;
    }

    public interface OnBackPressListener {
        void onBackPress();
    }

    public interface OnChooseListener {
        void onDone(String query);
    }

}
