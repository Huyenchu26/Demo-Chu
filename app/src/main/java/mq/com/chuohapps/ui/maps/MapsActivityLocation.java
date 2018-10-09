package mq.com.chuohapps.ui.maps;

import android.app.Fragment;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.socket.client.On;
import mq.com.chuohapps.R;
import mq.com.chuohapps.customview.OnClickListener;
import mq.com.chuohapps.data.helpers.network.response.Vehicle;
import mq.com.chuohapps.ui.history.event.ChangeDateEvent;
import mq.com.chuohapps.ui.home.dialog.DateDialog;
import mq.com.chuohapps.ui.xbase.BaseActivity;
import mq.com.chuohapps.ui.xbase.BaseFragment;
import mq.com.chuohapps.ui.xbase.container.ContainerActivity;
import mq.com.chuohapps.utils.AppUtils;
import mq.com.chuohapps.utils.data.DateUtils;
import mq.com.chuohapps.utils.functions.MessageUtils;

public class MapsActivityLocation extends BaseActivity<MapsConstract.Presenter> implements OnMapReadyCallback,
        MapsConstract.View, GoogleMap.OnMapLoadedCallback {

    private GoogleMap mMap;
    @BindView(R.id.textTitle)
    TextView textTitle;
    @BindView(R.id.imageBack)
    ImageView imageBack;
    @BindView(R.id.imageRight)
    ImageView imageRight;
    @BindView(R.id.texttime)
    TextView textTime;

    @Override
    protected int provideLayout() {
        return R.layout.activity_maps;
    }

    @Override
    protected Class<MapsConstract.Presenter> providePresenter() {
        return MapsConstract.Presenter.class;
    }

    String imei = "";
    String longitude, latitude;

    SupportMapFragment mapFragment;

    @Override
    protected void setupViews() {
        imei = getIntent().getStringExtra("imei");
        longitude = getIntent().getStringExtra("longi");
        latitude = getIntent().getStringExtra("lati");
        logError("init: " + longitude + " - " + latitude);
        setupHeader();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        initMaps();
    }

    private void initMaps() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    private void setupHeader() {
        textTitle.setVisibility(View.VISIBLE);
        textTitle.setText(getString(R.string.title_location));
        imageRight.setVisibility(View.VISIBLE);
        textTime.setVisibility(View.VISIBLE);
        textTime.setText(imei);
        imageRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onDelayedClick(View v) {
                openDateDialog();
            }
        });
        imageBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onDelayedClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void beginFlow() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        updateMaps();
    }

    @Override
    public void onMapLoaded() {
        updateMaps();
    }

    private void updateMaps() {
        logError(longitude + " - " + latitude);
        LatLng location = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
        MarkerOptions marker = new MarkerOptions();
        marker.position(location).draggable(true);
        mMap.addMarker(marker);
        CameraPosition camera = new CameraPosition.Builder()
                .target(location)
                .zoom(18)  //limite ->21
                .bearing(0) // 0 - 365
                .tilt(45) // limite ->90
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }

    @Override
    public void onStartGetListLocation() {
        showLoading();
    }

    @Override
    public void onGetListLocationSuccess(List<Vehicle> latLngs) {
        hideLoading();
    }

    @Override
    public void onGetListLocationError(String message) {
        hideLoading();
    }

    private void doLoadData() {
    }

    DateDialog dateDialog;

    private void openDateDialog() {
        if (dateDialog != null && dateDialog.isShowing()) return;
        dateDialog = new DateDialog(this);
        dateDialog.setCanceledOnTouchOutside(true);
        dateDialog.setOnChooseListener(new DateDialog.OnChooseListener() {
            @Override
            public void onDone(String startDate_, String endDate_) {
                // TODO: 4/19/2018 some thing with dates
                doLoadData();
            }
        });
        dateDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dateDialog.release();
            }
        });
        dateDialog.show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
