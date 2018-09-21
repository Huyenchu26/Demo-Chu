package mq.com.chuohapps.ui.maps;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mq.com.chuohapps.R;
import mq.com.chuohapps.customview.OnClickListener;
import mq.com.chuohapps.ui.xbase.BaseActivity;
import mq.com.chuohapps.ui.xbase.BaseFragment;
import mq.com.chuohapps.utils.data.DateUtils;
import mq.com.chuohapps.utils.functions.MessageUtils;

public class MapsActivity extends BaseActivity<MapsConstract.Presenter> implements OnMapReadyCallback, MapsConstract.View {

    private GoogleMap mMap;
    @BindView(R.id.textTitle)
    TextView textTitle;
    @BindView(R.id.imageBack)
    ImageView imageBack;

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
    @Override
    protected void setupViews() {
        setupHeader();
        setupDate();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
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

    @Override
    protected void beginFlow() {

    }

    private void setupHeader() {
        textTitle.setVisibility(View.VISIBLE);
        textTitle.setText("Location");
        imageBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onDelayedClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerEventBus();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterEventBus();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        PolylineOptions polylineOptions = new PolylineOptions();
        for (int i = 0; i < latLngs.size(); i++) {
            polylineOptions.add(latLngs.get(i));
        }
        LatLng location = latLngs.get(0);
//        CameraUpdate zoom = CameraUpdateFactory.zoomTo(20);
//        mMap.animateCamera(zoom);
//        mMap.addMarker(new MarkerOptions().position(location).title("This imei in here"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));

        MarkerOptions marker = new MarkerOptions()
                .position(location)
                .title("This imei in here")
                .draggable(true);

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


    @Override
    public void onStartGetListLocation() {
        showLoading();
    }

    @Override
    public void onGetListLocationSuccess(List<LatLng> latLngs) {
        hideLoading();
        if (latLngs == null || latLngs.size() == 0) return;
        this.latLngs.addAll(latLngs);
    }

    @Override
    public void onGetListLocationError(String message) {
        hideLoading();
        showMessage(message, MessageUtils.ERROR_CODE);
    }

    private void doLoadData(){
//        getPresenter().getListLocation(imei, startDate, endDate);
        latLngs.add(new LatLng(21.000488, 105.798337));
        latLngs.add(new LatLng(21.003807, 105.802491));
        latLngs.add(new LatLng(21.006772, 105.806568));
        latLngs.add(new LatLng(21.013823, 105.813005));
        latLngs.add(new LatLng(21.022476, 105.819142));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Object event) {

    }

}
