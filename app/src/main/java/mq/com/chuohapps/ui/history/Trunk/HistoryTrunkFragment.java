package mq.com.chuohapps.ui.history.Trunk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mq.com.chuohapps.R;
import mq.com.chuohapps.data.helpers.network.response.Vehicle;
import mq.com.chuohapps.ui.history.Trunk.adapter.TrunkAdapter;
import mq.com.chuohapps.utils.HistoryUtil;

public class HistoryTrunkFragment extends Fragment {

    @BindView(R.id.recyclerViewTrunk)
    RecyclerView recyclerViewTrunk;
    @BindView(R.id.txtTrunktime)
    TextView txtTrunktime;

    String startDate, endDate;
    List<Vehicle> vehicleList = new ArrayList<>();
    String imei;

    TrunkAdapter trunkAdapter;
    private View view;
    Unbinder unbinder;

    public static HistoryTrunkFragment newInstance(String imei, List<Vehicle> vehicleList,
                                                   String startDate, String endDate) {
        return new HistoryTrunkFragment().setDate(imei, vehicleList, startDate, endDate);
    }

    public HistoryTrunkFragment setDate(String imei, List<Vehicle> vehicleList, String startDate, String endDate) {
        this.vehicleList = vehicleList;
        this.imei = imei;
        this.startDate = startDate;
        this.endDate = endDate;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_history_trunk, container, false);
        unbinder = ButterKnife.bind(this, view);
        txtTrunktime.setText("Time: " + startDate + " - " + endDate);
        setupAdapter();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setupAdapter() {
        trunkAdapter = new TrunkAdapter();
        List<Vehicle> lists = HistoryUtil.getTimeLine(vehicleList);
        List<HistoryUtil.ItemTrunk> itemTrunks = new ArrayList<>();
        for (int i = 0; i < lists.size() - 2; i += 2) {
            itemTrunks.add(HistoryUtil.getItemTrunk(lists.get(i), lists.get(i + 1)));
        }
        trunkAdapter.addData(itemTrunks);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewTrunk.setLayoutManager(mLayoutManager);
        recyclerViewTrunk.setAdapter(trunkAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        unbinder = null;
    }
}
