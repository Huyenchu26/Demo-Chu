package mq.com.chuohapps.ui.history.CPUTime;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mq.com.chuohapps.R;
import mq.com.chuohapps.customview.LoadMoreRecyclerView;
import mq.com.chuohapps.data.helpers.network.response.Vehicle;
import mq.com.chuohapps.lib.swiperefresh.AppRefresher;
import mq.com.chuohapps.lib.swiperefresh.Refresher;
import mq.com.chuohapps.ui.history.CPUTime.adapter.CPUtimeAdapter;
import mq.com.chuohapps.ui.history.event.ChangeDateEvent;
import mq.com.chuohapps.ui.xbase.BaseFragment;
import mq.com.chuohapps.utils.HistoryUtil;


/**
 * A simple {@link Fragment} subclass.
 */
public class CPUFragment extends BaseFragment<CPUContract.Presenter> implements CPUContract.View {

    @BindView(R.id.listViewCPUtime)
    LoadMoreRecyclerView listViewCPUtime;

    CPUtimeAdapter adapter;
    List<Vehicle> vehicleList = new ArrayList<>();
    private Refresher refresher = new AppRefresher();

    public static CPUFragment newInstance(List<Vehicle> vehicleList) {
        return new CPUFragment().setDate(vehicleList);
    }

    public CPUFragment setDate(List<Vehicle> vehicleList){
        this.vehicleList = vehicleList;
        return this;
    }

    @Override
    protected int provideLayout() {
        return R.layout.fragment_history_cpu;
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
        adapter = new CPUtimeAdapter();
        parseVehicleToCPUTime(vehicleList);
        listViewCPUtime.setAdapter(adapter);
    }

    private void parseVehicleToCPUTime(List<Vehicle> vehicleList) {
        List<Vehicle> vehicles = HistoryUtil.getListRestartCPU(vehicleList);
        adapter.clearData();
        adapter.addData(vehicles);
        logError("parseVehicleToCPUTime");
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
        parseVehicleToCPUTime(event.vehicleList);
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
        if (listViewCPUtime.isLoading()) {
            return;
        } else {
            adapter.clearData();
            listViewCPUtime.refreshLoadMore();
            parseVehicleToCPUTime(vehicleList);
        }
    }
}
