package mq.com.chuohapps.ui.home;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.stetho.common.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import mq.com.chuohapps.MapsActivity;
import mq.com.chuohapps.R;
import mq.com.chuohapps.customview.LoadMoreRecyclerView;
import mq.com.chuohapps.customview.OnClickListener;
import mq.com.chuohapps.data.helpers.network.response.Vehicle;
import mq.com.chuohapps.ui.history.container.HistoryContainerFragment;
import mq.com.chuohapps.ui.home.adapter.VehicleAdapter;
import mq.com.chuohapps.ui.home.dialog.RFIDDialog;
import mq.com.chuohapps.ui.xbase.BaseFragment;

public class HomeFragment extends BaseFragment<HomeContract.Presenter> implements HomeContract.View {

    @BindView(R.id.editSearchQuery)
    EditText editSearchQuery;
    @BindView(R.id.recyclerviewMain)
    LoadMoreRecyclerView recyclerViewVehicle;
    @BindView(R.id.imageBack)
    ImageView imageBack;
    @BindView(R.id.relativeSearchLayout)
    RelativeLayout relativeSearchLayout;
    @BindView(R.id.imageSearchCancel)
    ImageView imageSearchCancel;
    @BindView(R.id.imageSearchClearText)
    ImageView imageSearchClearText;
    @BindView(R.id.imageRight)
    ImageView imageRight;


    VehicleAdapter adapter;
    List<Vehicle> vehiclesSearch = new ArrayList<>();

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected int provideLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected Class<HomeContract.Presenter> providePresenter() {
        return HomeContract.Presenter.class;
    }

    @Override
    protected void setupNavigationTitle() {

    }

    @Override
    protected void setupViews(@NonNull View view) {
        init();
        setHeaderTextColor(R.color.colorTextWhiteSecond);
        setupAdapter();
//        getPresenter().getVehicle();
        callAsynchronousTask();
        setupSearch();
    }

    private void init() {
        enableHeader("Vehicle");
        imageBack.setImageResource(R.mipmap.ic_option);
        relativeSearchLayout.setVisibility(View.VISIBLE);
        imageRight.setVisibility(View.VISIBLE);
    }

    private void setupAdapter() {
        adapter = new VehicleAdapter();
        recyclerViewVehicle.setAdapter(adapter);
        adapter.setItemListener(new VehicleAdapter.ItemListener() {
            @Override
            public void onRetryClick() {
            }

            @Override
            public void onImageLocationClick(String longitude, String latitude) {
                Intent intent = new Intent(myActivity(), MapsActivity.class);
                intent.putExtra("longitude", longitude);
                intent.putExtra("latitude", latitude);
                startActivity(intent);
            }

            @Override
            public void onOpenDialogRfid(List<String> rfid) {
                openDialogRfid(rfid);
            }

            @Override
            public void onItemListener(Vehicle.Data vehicle) {
                goToScreen(HistoryContainerFragment.newInstance(vehicle.getImei()));
            }
        });
    }

    RFIDDialog rfidDialog;

    private void openDialogRfid(List<String> rfid) {
        if (rfidDialog != null && rfidDialog.isShowing()) return;
        rfidDialog = new RFIDDialog(myActivity(), rfid);
        rfidDialog.setCanceledOnTouchOutside(true);
        rfidDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                rfidDialog.release();
            }
        });
        rfidDialog.show();
    }


    @Override
    protected void beginFlow(@NonNull View view) {

    }


    @Override
    public void onStartGetVehicle() {

    }

    @Override
    public void onGetVehicleSuccess(Vehicle vehicle) {

//        vehicles.clear();
//        vehicles.addAll(vehicle);
//        adapter.clearData();
//        adapter.addData(vehicles);

        LogUtil.e("isSuccessful: " + vehicle);
    }

    @Override
    public void onGetVehicleError(String message) {
        showMessage(message);
    }

    private void setupSearch() {
        setHeaderRightButton(R.mipmap.ic_search_black, new OnClickListener() {
            @Override
            public void onDelayedClick(View v) {
                String strSearch = editSearchQuery.getText().toString().trim();
                clearData();
                for (Vehicle vehicle : vehicles) {
                    if (vehicle.data.getImei().contains(strSearch)) {
                        vehiclesSearch.add(vehicle);
                    }
                }
                adapter.addData(vehiclesSearch);
            }
        });
        imageSearchClearText.setOnClickListener(new OnClickListener() {
            @Override
            public void onDelayedClick(View v) {
                editSearchQuery.setText(null);
            }
        });
        imageSearchCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onDelayedClick(View v) {
                editSearchQuery.setText(null);
                clearData();
                adapter.addData(vehicles);
            }
        });
    }

    private void clearData() {
        vehiclesSearch.clear();
        adapter.clearData();
    }


    List<Vehicle> vehicles = new ArrayList<>();

//7:cờ SOS (0: không có, 1: có SOS), 8: cờ mở cửa két xe (0: đóng/1 : mở),
// 9:cờ động cơ (bật/tắt), 10: cờ dừng đỗ, 11: cờ GPS (0: có, 1:mất GPS)

    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            getPresenter().getVehicle();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 30000); //execute in every 50000 ms
    }

}
