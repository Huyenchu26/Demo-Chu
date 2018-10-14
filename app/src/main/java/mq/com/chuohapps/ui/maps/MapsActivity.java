package mq.com.chuohapps.ui.maps;

import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

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
import mq.com.chuohapps.utils.data.DateUtils;
import mq.com.chuohapps.utils.functions.MessageUtils;

public class MapsActivity extends BaseActivity<MapsConstract.Presenter> implements OnMapReadyCallback,
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
    String startDate = null;
    String endDate = null;
    List<LatLng> latLngs = new ArrayList<>();

    SupportMapFragment mapFragment;
    @Override
    protected void setupViews() {
        setupDate();
        setupHeader();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        initMaps();
    }

    private void initMaps() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    private void setupDate() {
        Date dateCurrent = Calendar.getInstance().getTime();
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        imei = getIntent().getStringExtra("imei");
        startDate = DateUtils.dateToStringSent(new Date(dateCurrent.getTime() - (3 * DAY_IN_MS)));
        endDate = DateUtils.dateToStringSent(dateCurrent);
        doLoadData();
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

    boolean checkLoadMaps = false;
    @Override
    public void onMapLoaded() {
        checkLoadMaps = true;
        updateMaps();
    }

    private void updateMaps() {
        if (checkLoadMaps = true) {
            mMap.clear();
//            Double HeadingRotation = SphericalUtil.computeHeading(LatLng from, LatLng to)
            PolylineOptions polylineOptions = new PolylineOptions();
            if (latLngs != null && latLngs.size() > 0) {
                for (int i = 0; i < latLngs.size(); i++) {
                    polylineOptions.add(latLngs.get(i));
                }
                MarkerOptions marker = new MarkerOptions();
                LatLng location = latLngs.get(0);
                marker.position(location);

                marker.position(location).draggable(true);
                mMap.addMarker(marker);
                CameraPosition camera = new CameraPosition.Builder()
                        .target(location)
                        .zoom(18)  //limite ->21
                        .bearing(0) // 0 - 365
                        .tilt(45) // limite ->90
                        .build();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));

                polylineOptions.width(5).color(getResources().getColor(R.color.colorAccent)).geodesic(true);
                mMap.addPolyline(polylineOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));
            }
        }

    }

    @Override
    public void onStartGetListLocation() {
        showLoading();
    }

    @Override
    public void onGetListLocationSuccess(List<Vehicle> latLngs) {
        hideLoading();
        this.latLngs.clear();
        if (latLngs == null || latLngs.size() == 0) return;
        for (int i = 0; i < latLngs.size(); i++) {
            double lati = Double.valueOf(latLngs.get(i).latitude);
            double longi = Double.valueOf(latLngs.get(i).longitude);
            LatLng latLng = new LatLng(lati, longi);
            if (!(lati == 0 && longi == 0))
                this.latLngs.add(latLng);
            logError(latLng.latitude + " - " + latLng.longitude);
        }
        updateMaps();
        showMessage(startDate + " - " + endDate);
    }

    @Override
    public void onGetListLocationError(String message) {
        hideLoading();
        showMessage(message, MessageUtils.ERROR_CODE);
    }

    int count = 0;

    private void doLoadData() {
        count++;
        getPresenter().getListLocation(imei.substring(1), startDate, endDate);
    }

    DateDialog dateDialog;

    private void openDateDialog() {
        if (dateDialog != null && dateDialog.isShowing()) return;
        dateDialog = new DateDialog(this);
        dateDialog.setCanceledOnTouchOutside(true);
        dateDialog.setOnChooseListener(new DateDialog.OnChooseListener() {
            @Override
            public void onDone(String startDate_) {
                // TODO: 4/19/2018 some thing with dates
                startDate = startDate_ + " 00:00:00";
                endDate = startDate_ + " 23:59:59";
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
