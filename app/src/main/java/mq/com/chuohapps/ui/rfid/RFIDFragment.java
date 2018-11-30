package mq.com.chuohapps.ui.rfid;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import mq.com.chuohapps.R;
import mq.com.chuohapps.customview.LoadMoreRecyclerView;
import mq.com.chuohapps.customview.OnClickListener;
import mq.com.chuohapps.data.helpers.network.response.RFIDModel;
import mq.com.chuohapps.data.helpers.network.response.Vehicle;
import mq.com.chuohapps.ui.home.dialog.DateDialog;
import mq.com.chuohapps.ui.maps.dialog.DateEndImeiDialog;
import mq.com.chuohapps.ui.rfid.adapter.RFIDAdapter;
import mq.com.chuohapps.ui.xbase.BaseFragment;
import mq.com.chuohapps.utils.data.DateUtils;
import mq.com.chuohapps.utils.functions.MessageUtils;

public class RFIDFragment extends BaseFragment<RFIDContract.Presenter> implements RFIDContract.View {

    @BindView(R.id.listRFID)
    LoadMoreRecyclerView listRFID;
    @BindView(R.id.imageBack)
    ImageView imageBack;
    @BindView(R.id.imageRight)
    ImageView imageRight;
    @BindView(R.id.textTitle)
    TextView textTitle;

    RFIDAdapter adapter;

    private String imei;
    String startDate = null;
    String endDate = null;

    public RFIDFragment setImei(String imei) {
        this.imei = imei;
        return this;
    }

    public static RFIDFragment newInstance(String imei) {
        return new RFIDFragment().setImei(imei);
    }

    @Override
    protected int provideLayout() {
        return R.layout.fragment_rfid;
    }

    @Override
    protected Class<RFIDContract.Presenter> providePresenter() {
        return RFIDContract.Presenter.class;
    }

    @Override
    protected void setupNavigationTitle() {

    }

    @Override
    protected void setupViews(@NonNull View view) {
        imageBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onDelayedClick(View v) {
                moveBack();
            }
        });
        imageRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onDelayedClick(View v) {
                openDateDialog();
            }
        });
        textTitle.setText("RFID");
        adapter = new RFIDAdapter();
        listRFID.setAdapter(adapter);
    }

    private void setupDate() {
        Date dateCurrent = Calendar.getInstance().getTime();
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        startDate = DateUtils.dateToStringSent(new Date(dateCurrent.getTime() - (DAY_IN_MS)));
        endDate = DateUtils.dateToStringSent(dateCurrent);
        doLoadData();
    }

    private void doLoadData() {
        if (imei.length() > 0)
            getPresenter().getRFID(imei.substring(1), startDate, endDate);
        else return;
    }


    @Override
    protected void beginFlow(@NonNull View view) {
        setupDate();
    }

    @Override
    public void onStartGetRFID() {
//        showLoading();
    }

    @Override
    public void onGetRFIDSuccess(List<RFIDModel> vehicles) {
        hideLoading();
        if (vehicles != null) {
            adapter.addData(vehicles);
        }
    }

    @Override
    public void onGetFRIDFailed(String message) {
        hideLoading();
        showMessage(message, MessageUtils.ERROR_CODE);
    }

    DateEndImeiDialog dateDialog;

    private void openDateDialog() {
        if (dateDialog != null && dateDialog.isShowing()) return;
        dateDialog = new DateEndImeiDialog(getContext(), imei);
        dateDialog.setCanceledOnTouchOutside(true);
        dateDialog.setOnChooseListener(new DateEndImeiDialog.OnChooseListener() {
            @Override
            public void onDone(String startDate_, String imei_) {
                // TODO: 4/19/2018 some thing with dates
                startDate = startDate_ + " 00:00:00";
                endDate = startDate_ + " 23:59:59";
                imei = imei_;
                doLoadData();
            }
        });
        dateDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dateDialog.release();
            }
        });
        dateDialog.show();
    }
}
