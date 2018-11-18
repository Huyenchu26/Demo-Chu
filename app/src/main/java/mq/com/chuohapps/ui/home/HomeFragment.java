package mq.com.chuohapps.ui.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import mq.com.chuohapps.customview.TextChangedListener;
import mq.com.chuohapps.data.helpers.network.response.GetImeiSavedResponse;
import mq.com.chuohapps.data.helpers.network.response.SaveImeiResponse;
import mq.com.chuohapps.lib.swiperefresh.AppRefresher;
import mq.com.chuohapps.lib.swiperefresh.Refresher;
import mq.com.chuohapps.ui.home.dialog.UpdateNumberCarDialog;
import mq.com.chuohapps.ui.maps.MapsActivity;
import mq.com.chuohapps.R;
import mq.com.chuohapps.customview.LoadMoreRecyclerView;
import mq.com.chuohapps.customview.OnClickListener;
import mq.com.chuohapps.data.helpers.network.response.Vehicle;
import mq.com.chuohapps.ui.history.container.HistoryContainerFragment;
import mq.com.chuohapps.ui.home.adapter.VehicleAdapter;
import mq.com.chuohapps.ui.home.dialog.RFIDDialog;
import mq.com.chuohapps.ui.maps.MapsActivityLocation;
import mq.com.chuohapps.ui.rfid.RFIDFragment;
import mq.com.chuohapps.ui.xbase.BaseFragment;
import mq.com.chuohapps.utils.AppLogger;
import mq.com.chuohapps.utils.data.DateUtils;
import mq.com.chuohapps.utils.functions.MessageUtils;

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
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.optionImei)
    TextView optionImei;
    @BindView(R.id.optionSize)
    TextView optionSize;
    @BindView(R.id.optionDate)
    TextView optionDate;

    VehicleAdapter adapter;
    List<Vehicle> vehiclesSearch = new ArrayList<>();

    // TODO: 10/7/2018 option to choise: 0: currrent tab, 1: history, 2: direction
    private int option = 0;
    private int curMonth = Calendar.getInstance().getTime().getMonth();

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
        setupSearch();
        setupSort();
    }

    private int sortOption = 0;
    private int sortOption0 = 0, sortOption1 = 0, sortOption2 = 0;

    private void setupSort() {
        optionImei.setOnClickListener(new OnClickListener() {
            @Override
            public void onDelayedClick(View v) {
                sortOption0++;
                sortImei();
            }
        });
        optionDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onDelayedClick(View v) {
                sortOption1++;
                sortDate();
            }
        });
        optionSize.setOnClickListener(new OnClickListener() {
            @Override
            public void onDelayedClick(View v) {
                sortOption2++;
                sortSize();
            }
        });
    }

    private void sortSize() {
        sortOption = 2;
        optionImei.setTextColor(getResources().getColor(R.color.colorTextPrimary));
        optionDate.setTextColor(getResources().getColor(R.color.colorTextPrimary));
        optionSize.setTextColor(getResources().getColor(R.color.colorFacebook));
        Collections.sort(vehicles, Vehicle.VehicleSize);
        if (sortOption2 % 2 == 0)
            Collections.reverse(vehicles);
        adapter.clearData();
        adapter.addData(vehicles);
    }

    private void sortDate() {
        sortOption = 1;
        optionImei.setTextColor(getResources().getColor(R.color.colorTextPrimary));
        optionDate.setTextColor(getResources().getColor(R.color.colorFacebook));
        optionSize.setTextColor(getResources().getColor(R.color.colorTextPrimary));
        Collections.sort(vehicles, Vehicle.VehicleDate);
        if (sortOption1 % 2 == 1)
            Collections.reverse(vehicles);
        adapter.clearData();
        adapter.addData(vehicles);
    }

    private void sortImei() {
        sortOption = 0;
        optionImei.setTextColor(getResources().getColor(R.color.colorFacebook));
        optionDate.setTextColor(getResources().getColor(R.color.colorTextPrimary));
        optionSize.setTextColor(getResources().getColor(R.color.colorTextPrimary));
        Collections.sort(vehicles, Vehicle.VehicleImei);
        if (sortOption0 % 2 == 0)
            Collections.reverse(vehicles);
        adapter.clearData();
        adapter.addData(vehicles);
    }

    private void doLoadData() {
        isGetDataDone = false;
        getPresenter().getVehicle();
    }

    private void init() {
        enableHeader(getString(R.string.title_vehicle));
        imageBack.setImageResource(R.mipmap.ic_option);
        relativeSearchLayout.setVisibility(View.VISIBLE);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.nav_direction:
//                        option = 2;
                        Intent intent = new Intent(myActivity(), MapsActivity.class);
                        intent.putExtra("imei", adapter.getFirstItem().imei);
                        startActivity(intent);
                        break;
                    case R.id.nav_month:
                        option = 3;
                        break;
                    case R.id.nav_rfid:
                        goToScreen(new RFIDFragment());
                    default:
                        option = 0;
                        break;
                }
                return true;
            }
        });
        optionImei.setTextColor(getResources().getColor(R.color.colorFacebook));
        imageBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onDelayedClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        imageRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onDelayedClick(View v) {
                if (editSearchQuery.getText() != null)
                    showKeyboard(editSearchQuery);
                else {
                    doSearch(editSearchQuery.getText().toString().trim());
                }
            }
        });
    }

    private void doSearch(String textChanged) {
        clearData();
        for (Vehicle vehicle : vehicles) {
            if (vehicle.imei.contains(textChanged)) {
                vehiclesSearch.add(vehicle);
            }
        }
        adapter.addData(vehiclesSearch);
    }

    private Refresher refresher = new AppRefresher();

    private void setupAdapter() {
        adapter = new VehicleAdapter();
        adapter.setItemListener(new VehicleAdapter.ItemListener() {
            @Override
            public void onRetryClick() {

            }

            @Override
            public void onImageLocationClick(String imei, String longi, String lati) {
                Intent intent = new Intent(myActivity(), MapsActivityLocation.class);
                intent.putExtra("imei", imei);
                intent.putExtra("longi", longi);
                intent.putExtra("lati", lati);
                logError(longi + " - " + lati);
                startActivity(intent);
            }

            @Override
            public void onOpenDialogRfid(List<String> rfid) {
                openDialogRfid(rfid);
            }

            @Override
            public void onItemListener(Vehicle vehicle) {
                if (option == 1 || option == 0) {
                    goToScreen(HistoryContainerFragment.newInstance(vehicle.imei, curMonth));
                } else {
                    // TODO: 10/7/2018 set data for curDate in here

                }
            }

            @Override
            public void onItemLongClick(String imei, String number) {
                openDialogNumberCar(imei, number);
                imeiIndex = imei;
            }
        });
        recyclerViewVehicle.setAdapter(adapter);
    }

    private String imeiIndex = "";
    private String numberCar = "";
    UpdateNumberCarDialog updateDataDialog;

    private void openDialogNumberCar(final String imei, String number) {
        updateDataDialog = new UpdateNumberCarDialog(myActivity(), number);
        updateDataDialog.setOnChooseListener(new UpdateNumberCarDialog.OnChooseListener() {
            @Override
            public void onDone(String query) {
                getPresenter().saveImei(imei, query);
                numberCar = query;
            }
        });
        updateDataDialog.setCanceledOnTouchOutside(true);
        updateDataDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                updateDataDialog.release();
            }
        });
        updateDataDialog.show();
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
    public void onStart() {
        super.onStart();
        bindRefresh();
    }

    @Override
    public void onStop() {
        super.onStop();
        refresher.release();
    }

    private boolean isGetDataDone = false;
    @Override
    public void onGetVehicleSuccess(List<Vehicle> vehicle) {
        hideLoading();
        getPresenter().getSavedImei();
        isGetDataDone = true;
        vehicles.addAll(vehicle);
        logError("do onGetVehicleSuccess");

        String str = editSearchQuery.getText().toString().trim();
        if (str.length() > 0) {
            doSearch(str);
        } else {
            switch (sortOption) {
                case 0:
                    sortImei();
                    break;
                case 1:
                    sortDate();
                    break;
                case 2:
                    sortSize();
                    break;
                default:
                    sortImei();
                    break;
            }
//            Collections.sort(vehicles, Vehicle.VehicleImei);
//            adapter.addData(vehicles);
        }
    }

    @Override
    public void onGetVehicleError(String message) {
        hideLoading();
        getPresenter().getSavedImei();
        showMessage(message, MessageUtils.ERROR_CODE);
        logError("do onGetVehicleError");
    }

    private void setupSearch() {
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
                doSearch(textChanged);
//                adapter.getFilter().filter(textChanged);
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
        if (recyclerViewVehicle.isLoading()) {
            return;
        } else {
            adapter.clearData();
            vehiclesSearch.clear();
            vehicles.clear();
            recyclerViewVehicle.refreshLoadMore();
            doLoadData();
        }
    }

    // TODO: 10/13/2018 save imei car

    @Override
    public void onStartSaveImei() {
        showLoading();
    }

    @Override
    public void onSaveImeiSuccess(SaveImeiResponse response) {
        hideLoading();
//        for (int i = 0; i < vehiclesSearch.size(); i++) {
//            if (imeiIndex.equals(vehiclesSearch.get(i).imei)) {
//                vehiclesSearch.remove(i);
//                adapter.delItem(i);
//            }
//        }
        adapter.updateItemDone(recyclerViewVehicle, imeiIndex, numberCar);
        logError("number car: " + numberCar);
        MessageUtils.show(myActivity(), "success!", MessageUtils.SUCCESS_CODE);
    }

    @Override
    public void onSaveImeiFailed(String message) {
        hideLoading();
//        MessageUtils.show(myActivity(), message, MessageUtils.ERROR_CODE);
        adapter.updateItemDone(recyclerViewVehicle, imeiIndex, numberCar);
        MessageUtils.show(myActivity(), "success!", MessageUtils.ERROR_CODE);
    }

    // TODO: 10/14/2018 get Imei saved

    @Override
    public void onStartGetImei() {
//        showLoading();
    }

    @Override
    public void onGetImeiSuccess(GetImeiSavedResponse response) {
        hideLoading();
        if (vehiclesSearch.size() > 0) {
            for (int i = 0; i < vehiclesSearch.size(); i++) {
                for (int j = 0; j < response.result.size(); j++) {
                    if (vehiclesSearch.get(i).imei.equals(response.result.get(j)))
                        adapter.updateItemDone(recyclerViewVehicle,
                                vehiclesSearch.get(i).imei, response.listnumber.get(j));
                }
            }
        } else for (int i = 0; i < vehicles.size(); i++) {
            for (int j = 0; j < response.result.size(); j++) {
                if (vehicles.get(i).imei.equals(response.result.get(j)))
                    adapter.updateItemDone(recyclerViewVehicle, vehicles.get(i).imei, response.listnumber.get(j));
            }
        }
        MessageUtils.show(myActivity(), "onGetImeiSuccess!", MessageUtils.SUCCESS_CODE);
    }

    @Override
    public void onGetImeiFailed(String message) {
        hideLoading();
        MessageUtils.show(myActivity(), "onGetImeiFailed!", MessageUtils.ERROR_CODE);
    }
}
