package mq.com.chuohapps.ui.rfid;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import mq.com.chuohapps.R;
import mq.com.chuohapps.customview.LoadMoreRecyclerView;
import mq.com.chuohapps.customview.OnClickListener;
import mq.com.chuohapps.data.helpers.network.response.Vehicle;
import mq.com.chuohapps.ui.rfid.adapter.RFIDAdapter;
import mq.com.chuohapps.ui.xbase.BaseFragment;

public class RFIDFragment extends BaseFragment<RFIDContract.Presenter> implements RFIDContract.View {

    @BindView(R.id.listRFID)
    LoadMoreRecyclerView listRFID;
    @BindView(R.id.imageBack)
    ImageView imageBack;
    @BindView(R.id.imageRight)
    ImageView imageRight;
    @BindView(R.id.textTitle)
    TextView textTitle;

    List<Vehicle> vehicles = new ArrayList<>();
    RFIDAdapter adapter;

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
                // TODO: 11/20/2018 search
            }
        });
        textTitle.setText("RFID");
        adapter = new RFIDAdapter();
//        listRFID.setAdapter(adapter);
    }

    @Override
    protected void beginFlow(@NonNull View view) {
        getPresenter().getRFID("", "", "");
    }

    @Override
    public void onStartGetRFID() {

    }

    @Override
    public void onGetRFIDSuccess(List<Vehicle> vehicles) {

    }

    @Override
    public void onGetFRIDFailed(String message) {

    }
}
