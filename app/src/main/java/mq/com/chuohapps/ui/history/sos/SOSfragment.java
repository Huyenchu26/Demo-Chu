package mq.com.chuohapps.ui.history.sos;

import android.support.annotation.NonNull;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import mq.com.chuohapps.R;
import mq.com.chuohapps.customview.LoadMoreRecyclerView;
import mq.com.chuohapps.data.helpers.network.response.Vehicle;
import mq.com.chuohapps.lib.swiperefresh.AppRefresher;
import mq.com.chuohapps.lib.swiperefresh.Refresher;
import mq.com.chuohapps.ui.history.CPUTime.CPUContract;
import mq.com.chuohapps.ui.history.event.ChangeDateEvent;
import mq.com.chuohapps.ui.history.sos.adapter.SOSAdapter;
import mq.com.chuohapps.ui.xbase.BaseFragment;
import mq.com.chuohapps.utils.HistoryUtil;

public class SOSfragment extends BaseFragment<CPUContract.Presenter> implements CPUContract.View {

    @BindView(R.id.listViewSOS)
    LoadMoreRecyclerView listViewSOS;

    View view;
    SOSAdapter adapter;
    List<Vehicle> vehicleList = new ArrayList<>();
    private Refresher refresher = new AppRefresher();

    public static SOSfragment newInstance(List<Vehicle> vehicleList) {
        return new SOSfragment().setDate(vehicleList);
    }

    public SOSfragment setDate(List<Vehicle> vehicleList){
        this.vehicleList = vehicleList;
        return this;
    }

    @Override
    protected int provideLayout() {
        return R.layout.fragment_history_sos;
    }

    @Override
    protected Class<CPUContract.Presenter> providePresenter() {
        return CPUContract.Presenter.class;
    }

    @Override
    protected void setupNavigationTitle() {

    }

    @Override
    protected void setupViews(@NonNull View view) {
        setupAdapter();
    }

    @Override
    protected void beginFlow(@NonNull View view) {

    }

    private void setupAdapter() {
        adapter = new SOSAdapter();
        parseVehicleToSOS(vehicleList);
        listViewSOS.setAdapter(adapter);
    }

    private void parseVehicleToSOS(List<Vehicle> vehicleList) {
        List<Vehicle> vehicles = HistoryUtil.getListSOS(vehicleList);
        adapter.clearData();
        adapter.addData(vehicles);
        logError("parseVehicleToSOS: " + vehicles.size());
    }

    @Override
    public void onStart() {
        super.onStart();
        bindRefresh();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        refresher.release();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(ChangeDateEvent event) {
        parseVehicleToSOS(event.vehicleList);
    }

    private void bindRefresh() {
        refresher.setup(getView(), R.id.swipeRefresh);
        refresher.onBeginRefresh(new Runnable() {
            @Override
            public void run() {
                doRefresh();
            }
        });
    }

    private void doRefresh() {
        if (listViewSOS.isLoading()) {
            return;
        } else {
            adapter.clearData();
            listViewSOS.refreshLoadMore();
            parseVehicleToSOS(vehicleList);
        }
    }
}
