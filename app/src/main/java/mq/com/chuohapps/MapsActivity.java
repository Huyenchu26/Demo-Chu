package mq.com.chuohapps;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import mq.com.chuohapps.customview.OnClickListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    @BindView(R.id.textTitle)
    TextView textTitle;
    @BindView(R.id.imageBack)
    ImageView imageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ButterKnife.bind(this, MapsActivity.this);
        setupHeader();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        double longitude = getIntent().getDoubleExtra("longitude", 0);
        double latitude = getIntent().getDoubleExtra("latitude", 0);

        // Add a marker in Sydney and move the camera
        LatLng location = new LatLng(latitude, longitude);
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
    }
}
