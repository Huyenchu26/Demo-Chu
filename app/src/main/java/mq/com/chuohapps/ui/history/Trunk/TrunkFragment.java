package mq.com.chuohapps.ui.history.Trunk;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import mq.com.chuohapps.data.helpers.network.response.Vehicle;
import mq.com.chuohapps.ui.history.Trunk.adapter.TrunkAdapter;
import mq.com.chuohapps.ui.history.event.ChangeDateEvent;
import mq.com.chuohapps.ui.xbase.BaseFragment;
import mq.com.chuohapps.utils.HistoryUtil;

public class TrunkFragment extends BaseFragment<TrunkContract.Presenter> implements TrunkContract.View {

    @BindView(R.id.recyclerViewTrunk)
    RecyclerView recyclerViewTrunk;

    List<Vehicle> vehicleList = new ArrayList<>();
    String imei;

    TrunkAdapter trunkAdapter;
    private View view;
    Unbinder unbinder;

    public static TrunkFragment newInstance(List<Vehicle> vehicleList) {
        return new TrunkFragment().setDate(vehicleList);
    }

    public TrunkFragment setDate(List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
        return this;
    }

    @Override
    protected int provideLayout() {
        return R.layout.fragment_history_trunk;
    }

    @Override
    protected Class<TrunkContract.Presenter> providePresenter() {
        return TrunkContract.Presenter.class;
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
        trunkAdapter = new TrunkAdapter();

        parseVehicleToTrunkItem(vehicleList);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewTrunk.setLayoutManager(mLayoutManager);
        recyclerViewTrunk.setAdapter(trunkAdapter);
    }

    private void parseVehicleToTrunkItem(List<Vehicle> vehicleList) {
        List<Vehicle> lists = HistoryUtil.getTimeLine(vehicleList);
        List<HistoryUtil.ItemTrunk> itemTrunks = new ArrayList<>();
        for (int i = 0; i < lists.size() - 2; i += 2) {
            itemTrunks.add(HistoryUtil.getItemTrunk(lists.get(i), lists.get(i + 1)));
        }
        trunkAdapter.clearData();
        trunkAdapter.addData(itemTrunks);
        logError("parseVehicleToTrunkItem");
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(ChangeDateEvent event) {
        parseVehicleToTrunkItem(event.vehicleList);
    }
}
