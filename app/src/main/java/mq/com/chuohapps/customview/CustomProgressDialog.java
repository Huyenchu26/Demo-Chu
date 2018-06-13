package mq.com.chuohapps.customview;

import android.app.ProgressDialog;
import android.content.Context;

import mq.com.chuohapps.R;

public class CustomProgressDialog extends ProgressDialog {
    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    public void show() {
        super.show();
        setContentView(R.layout.x_layout_dialog_loading);
    }
}
