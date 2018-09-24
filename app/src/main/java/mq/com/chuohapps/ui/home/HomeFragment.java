package mq.com.chuohapps.ui.home;


import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import mq.com.chuohapps.customview.TextChangedListener;
import mq.com.chuohapps.ui.maps.MapsActivity;
import mq.com.chuohapps.R;
import mq.com.chuohapps.customview.LoadMoreRecyclerView;
import mq.com.chuohapps.customview.OnClickListener;
import mq.com.chuohapps.data.helpers.network.response.Vehicle;
import mq.com.chuohapps.ui.history.container.HistoryContainerFragment;
import mq.com.chuohapps.ui.home.adapter.VehicleAdapter;
import mq.com.chuohapps.ui.home.dialog.RFIDDialog;
import mq.com.chuohapps.ui.xbase.BaseFragment;
import mq.com.chuohapps.utils.AppLogger;

public class HomeFragment extends BaseFragment<HomeContract.Presenter> implements HomeContract.View {

    @BindView(R.id.editSearchQuery)
    EditText editSearchQuery;
    @BindView(R.id.recyclerviewMain)
    LoadMoreRecyclerView recyclerViewVehicle;
    @BindView(R.id.imageBack)
    ImageView imageBack;
    @BindView(R.id.relativeSearchLayout)
    RelativeLayout relativeSearchLayout;
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
        doLoadData();
//        callAsynchronousTask();
        setupSearch();
    }

    private void doLoadData() {
        getPresenter().getVehicle();
//        fakeData();
    }

    private void fakeData() {
        Vehicle vehicle1 = new Vehicle(new Vehicle.Data("192345234656321", "26-06-1996", "120", "120",
                "1", "0", "1", "1", "1", "1", "0", "1", "1",
                "12", "ffffffffff010100035b7189ffffffff010100035b8894ffffffffff01010003ab7129ffffffffff01" +
                "0100035be694ffffffffff010100035b7129ffffffffff010100035bcc94ffffffffff010100035b7129ffffffffff810100035bc" +
                "c94ffffffffff01010003597129ffffffffff010100066d3194ffffffffff01010003", "1", "1", "1", "123"));
        Vehicle vehicle2 = new Vehicle(new Vehicle.Data("18234489756321", "26-06-1996", "120", "120",
                "1", "0", "1", "1", "1", "1", "0", "1", "1",
                "12", "ff020180035b7129ffffffffff010100035bcc94ffffffff000100035b7129ffffffffff010100035b71" +
                "29ffffffffff010100035bcc94ffffffffff010100035b7129ffffffffff010100035bcccaffffffffff010100035bb129ffffffff" +
                "ff010100035bcc94ffffffffff010100035b7129ffffffffff010100035bcc94ff", "1", "1", "1", "1102"));
        Vehicle vehicle3 = new Vehicle(new Vehicle.Data("12346544563921", "26-06-1996", "120", "120",
                "1", "0", "1", "1", "1", "1", "0", "1", "1",
                "12", "ffffffffff010100035b7189ffffffff010100035b8894ffffffffff01010003ab7129ffffffffff01" +
                "0100035be694ffffffffff010100035b7129ffffffffff010100035bcc94ffffffffff010100035b7129ffffffffff810100035bc" +
                "c94ffffffffff01010003597129ffffffffff010100066d3194ffffffffff01010003", "1", "1", "1", "178"));
        Vehicle vehicle4 = new Vehicle(new Vehicle.Data("12349512316321", "26-06-1996", "120", "120",
                "1", "0", "1", "1", "1", "1", "0", "1", "1",
                "12", "ffffffffff010100035b7129ffffffffff010100035bcc94ffffffffff010100035b7129ffffffffff010" +
                "100022b7bcaffffffffff010100b39ecaffffffffff010100035bcc94ffffffffff010100035b71f9ffffffffff010100035bcc94f" +
                "fffffffff010100035b7129ffffffffff810100035bcc94ffffffffff01010003", "1", "1", "1", "1983"));

        vehicles.add(vehicle1);
        vehicles.add(vehicle2);
        vehicles.add(vehicle1);
        vehicles.add(vehicle3);
        vehicles.add(vehicle1);
        vehicles.add(vehicle4);
        vehicles.add(vehicle2);
        vehicles.add(vehicle3);
        vehicles.add(vehicle1);
        vehicles.add(vehicle4);
        vehicles.add(vehicle2);
        adapter.addData(vehicles);
    }

    private void init() {
        enableHeader(getString(R.string.title_vehicle));
        imageBack.setImageResource(R.mipmap.ic_option);
        imageBack.setOnClickListener(null);
        relativeSearchLayout.setVisibility(View.VISIBLE);
        imageRight.setVisibility(View.VISIBLE);
    }

    private void setupAdapter() {
        adapter = new VehicleAdapter();
//        recyclerViewVehicle.set
        adapter.setItemListener(new VehicleAdapter.ItemListener() {
            @Override
            public void onRetryClick() {
            }

            @Override
            public void onImageLocationClick(String imei) {
                Intent intent = new Intent(myActivity(), MapsActivity.class);
                intent.putExtra("imei", imei);
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
        recyclerViewVehicle.setAdapter(adapter);
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
//        showLoading();
    }

    @Override
    public void onGetVehicleSuccess(List<Vehicle> vehicle) {
        hideLoading();
        vehicles.clear();
        vehicles.addAll(vehicle);
        adapter.clearData();
        adapter.addData(vehicles);
        AppLogger.error("isSuccessful: " + vehicle);
    }

    @Override
    public void onGetVehicleError(String message) {
        hideLoading();
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
        editSearchQuery.addTextChangedListener(new TextChangedListener() {
            @Override
            public void onTextChanged(String textChanged) {
                // TODO: 9/21/2018 search imei
                clearData();
                for (Vehicle vehicle : vehicles) {
                    if (vehicle.data.getImei().contains(textChanged)) {
                        vehiclesSearch.add(vehicle);
                    }
                }
                adapter.addData(vehiclesSearch);
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
                            doLoadData();
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
