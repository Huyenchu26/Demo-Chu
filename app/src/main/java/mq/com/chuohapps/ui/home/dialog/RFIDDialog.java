package mq.com.chuohapps.ui.home.dialog;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import mq.com.chuohapps.R;
import mq.com.chuohapps.customview.MyAlertDialog;
import mq.com.chuohapps.ui.home.adapter.LineRfidAdapter;
import mq.com.chuohapps.utils.views.KeyboardUtils;

public class RFIDDialog extends MyAlertDialog {

    @BindView(R.id.container)
    View container;
    @BindView(R.id.listRfid)
    ListView listRfid;

    private List<String> rfid = new ArrayList<>();

    public RFIDDialog(Context context, List<String> rfid) {
        super(context);
        this.rfid = rfid;
    }

    @Override
    protected int provideLayout() {
        return R.layout.dialog_rfid;
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
        LineRfidAdapter rfidAdapter = new LineRfidAdapter(getContext(), rfid);
        listRfid.setAdapter(rfidAdapter);
    }

    public void release() {
        super.release();
    }

}