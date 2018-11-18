package mq.com.chuohapps.ui.rfid;

import android.support.annotation.NonNull;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import mq.com.chuohapps.R;
import mq.com.chuohapps.customview.LoadMoreRecyclerView;
import mq.com.chuohapps.data.helpers.network.response.Vehicle;
import mq.com.chuohapps.ui.rfid.adapter.RFIDAdapter;
import mq.com.chuohapps.ui.xbase.BaseFragment;

public class RFIDFragment extends BaseFragment<RFIDContract.Presenter> implements RFIDContract.View {

    @BindView(R.id.listRFID)
    LoadMoreRecyclerView listRFID;

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
        adapter = new RFIDAdapter();
        listRFID.setAdapter(adapter);
    }

    @Override
    protected void beginFlow(@NonNull View view) {

    }

    @Override
    public void onStartGetRFID() {

    }

    @Override
    public void onGetRFIDSuccess() {

    }

    @Override
    public void onGetFRIDFailed(String message) {

    }
}
