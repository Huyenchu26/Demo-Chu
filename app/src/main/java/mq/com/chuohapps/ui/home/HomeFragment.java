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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import mq.com.chuohapps.customview.FilterDialog;
import mq.com.chuohapps.customview.TextChangedListener;
import mq.com.chuohapps.lib.swiperefresh.AppRefresher;
import mq.com.chuohapps.lib.swiperefresh.Refresher;
import mq.com.chuohapps.ui.maps.MapsActivity;
import mq.com.chuohapps.R;
import mq.com.chuohapps.customview.LoadMoreRecyclerView;
import mq.com.chuohapps.customview.OnClickListener;
import mq.com.chuohapps.data.helpers.network.response.Vehicle;
import mq.com.chuohapps.ui.history.container.HistoryContainerFragment;
import mq.com.chuohapps.ui.home.adapter.VehicleAdapter;
import mq.com.chuohapps.ui.home.dialog.RFIDDialog;
import mq.com.chuohapps.ui.maps.MapsActivityLocation;
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
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

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
//        callAsynchronousTask();
        setupSearch();
    }

    private void doLoadData() {
        getPresenter().getVehicle();
    }

    private void init() {
        enableHeader(getString(R.string.title_vehicle));
        imageBack.setImageResource(R.mipmap.ic_option);
        relativeSearchLayout.setVisibility(View.VISIBLE);
        imageRight.setVisibility(View.VISIBLE);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();
                String id = String.valueOf(item.getItemId());
                switch (id.charAt(id.length() - 1)) {
                    case 1:
                        option = 1;
                        break;
                    case 2:
                        option = 2;
                        break;
                    case 3:
                        option = 3;
                        break;
                    default:
                        option = 0;
                        break;
                }
                logError("option: " + option);
                return true;
            }
        });
        imageBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onDelayedClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        imageRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onDelayedClick(View v) {
                showFilterDialog();
            }
        });
    }

    FilterDialog filterDialog;
    private void showFilterDialog() {
        logError("show filter dialog");
        if (filterDialog != null && filterDialog.isShowing()) return;
        filterDialog = new FilterDialog(myActivity());
        filterDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                filterDialog.release();
            }
        });
        filterDialog.setCanceledOnTouchOutside(true);
        filterDialog.show();
        logError("show show");
    }

    private Refresher refresher = new AppRefresher();

    private void setupAdapter() {
        adapter = new VehicleAdapter();
//        recyclerViewVehicle.setOnS
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
            public void onItemListener(Vehicle.Data vehicle) {
                if (option == 1 || option == 0) {
                    goToScreen(HistoryContainerFragment.newInstance(vehicle.getImei(), curMonth));
                } else if (option == 2) {
                    Intent intent = new Intent(myActivity(), MapsActivity.class);
                    intent.putExtra("imei", vehicle.getImei());
                    startActivity(intent);
                } else if (option == 3) {
                    // TODO: 10/7/2018 set data for curDate in here

                }
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
    public void onStart() {
        super.onStart();
        bindRefresh();
    }

    @Override
    public void onStop() {
        super.onStop();
        refresher.release();
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
            recyclerViewVehicle.refreshLoadMore();
            doLoadData();
        }
    }
}
