package mq.com.chuohapps.ui.home.dialog;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import mq.com.chuohapps.R;
import mq.com.chuohapps.customview.MyAlertDialog;
import mq.com.chuohapps.ui.home.adapter.LineRfidAdapter;
import mq.com.chuohapps.utils.views.KeyboardUtils;

public class OptionsDialog extends MyAlertDialog {

    @BindView(R.id.container)
    View container;
    @BindView(R.id.btnListRFID)
    Button btnListRFID;
    @BindView(R.id.btnDirection)
    Button btnDirection;
    @BindView(R.id.btnRFID)
    Button btnRFID;

    private List<String> rfid = new ArrayList<>();
    private String imei;

    public OptionsDialog(Context context, List<String> rfid, String imei) {
        super(context);
        this.rfid = rfid;
        this.imei = imei;
    }

    @Override
    protected int provideLayout() {
        return R.layout.dialog_options_main;
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
    }

    @OnClick(R.id.btnListRFID)
    public void onBtn1Click() {
        if (listener != null) {
            listener.onBtn1Click(rfid);
        }
        dismiss();
    }

    @OnClick(R.id.btnDirection)
    public void onBtn2Click() {
        if (listener != null) {
            listener.onBtn2Click(imei);
        }
        dismiss();
    }

    @OnClick(R.id.btnRFID)
    public void onBtn3Click() {
        if (listener != null) {
            listener.onBtn3Click(imei);
        }
        dismiss();
    }

    public void release() {
        super.release();
    }

    public interface ItemClickListener {
        void onBtn1Click(List<String> rfid);

        void onBtn2Click(String imei);

        void onBtn3Click(String imei);
    }

    public ItemClickListener listener;
    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }
}