package mq.com.chuohapps.ui.maps;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import mq.com.chuohapps.R;
import mq.com.chuohapps.customview.OnClickListener;
import mq.com.chuohapps.ui.demomaps.Data;
import mq.com.chuohapps.ui.demomaps.MapsActivityDemo;
import mq.com.chuohapps.ui.maps.dialog.DateEndImeiDialog;
import mq.com.chuohapps.ui.rfid.RFIDFragment;
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

    public static ProgressDialog dialog;

    public MapsActivity setImei(String imei) {
        this.curImei = imei;
        return this;
    }

    public static MapsActivity newInstance(String imei) {
        return new MapsActivity().setImei(imei);
    }

    @Override
    protected int provideLayout() {
        return R.layout.activity_maps;
    }

    @Override
    protected Class<MapsConstract.Presenter> providePresenter() {
        return MapsConstract.Presenter.class;
    }

    String curImei = "";
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
        long DAY_IN_MS = 3600000 * 24;
        curImei = getIntent().getStringExtra("imei");
        startDate = DateUtils.dateToStringSent(new Date(dateCurrent.getTime() - DAY_IN_MS));
        endDate = DateUtils.dateToStringSent(dateCurrent);
        doLoadData(curImei, startDate, endDate);
    }

    private void setupHeader() {
        textTitle.setVisibility(View.VISIBLE);
        textTitle.setText(getString(R.string.title_location));
        imageRight.setVisibility(View.VISIBLE);
        textTime.setVisibility(View.VISIBLE);
        textTime.setText(curImei);
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
//        if (checkLoadMaps = true) {
//            mMap.clear();
//            PolylineOptions polylineOptions = new PolylineOptions();
//            if (latLngs != null && latLngs.size() > 0) {
//                for (int i = 0; i < latLngs.size(); i++) {
//                    polylineOptions.add(latLngs.get(i));
//                }
//                MarkerOptions marker = new MarkerOptions();
//                LatLng location = latLngs.get(0);
//                marker.position(location);
//
//                marker.position(location).draggable(true);
//                mMap.addMarker(marker);
//                CameraPosition camera = new CameraPosition.Builder()
//                        .target(location)
//                        .zoom(18)  //limite ->21
//                        .bearing(0) // 0 - 365
//                        .tilt(45) // limite ->90
//                        .build();
//
//                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
//
//                polylineOptions.width(5).color(getResources().getColor(R.color.colorAccent)).geodesic(true);
//                mMap.addPolyline(polylineOptions);
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));
//            }
//        }

    }

    @Override
    public void onStartGetListLocation() {
        showLoading();
    }

    @Override
    public void onGetListLocationSuccess(List<String> latLngs) {
        hideLoading();
        this.latLngs.clear();
        if (latLngs == null || latLngs.size() == 0) return;
        for (int i = 0; i < latLngs.size(); i++) {
            String[] spl = latLngs.get(i).split("-");
            if (spl.length >= 2) {
                double lati = Double.valueOf(spl[0]);
                double longi = Double.valueOf(spl[0]);
                LatLng latLng = new LatLng(lati, longi);
                if (!(lati == 0 && longi == 0))
                    this.latLngs.add(latLng);
            }
        }
        updateMaps();
        showMessage(startDate + " - " + endDate);
    }

    @Override
    public void onGetListLocationError(String message) {
        hideLoading();
        showMessage(message, MessageUtils.ERROR_CODE);
    }

    private String url = "http://navistardev.cloudapp.net:5005/api/Values?imei=";
    private void doLoadData(String imei, String startDate, String endDate) {
//        if (curImei.length() > 1)
//            getPresenter().getListLocation(curImei.substring(1), startDate, endDate, false);
        url = url + imei + "&startDate=" + startDate + "&endDate=" + endDate;
        DirectionTask directionTask = new DirectionTask();
        directionTask.execute(url);
    }

    DateEndImeiDialog dateDialog;

    private void openDateDialog() {
        if (dateDialog != null && dateDialog.isShowing()) return;
        dateDialog = new DateEndImeiDialog(this, curImei);
        dateDialog.setCanceledOnTouchOutside(true);
        dateDialog.setOnChooseListener(new DateEndImeiDialog.OnChooseListener() {
            @Override
            public void onDone(String startDate_, String imei) {
                // TODO: 4/19/2018 some thing with dates
                startDate = startDate_ + " 00:00:00";
                endDate = startDate_ + " 23:59:59";
                curImei = imei;
                textTitle.setText(imei);
                doLoadData(curImei, startDate, endDate);
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


    private final double degreesPerRadian = 180.0 / Math.PI;

    private void DrawArrowHead(GoogleMap mMap, LatLng from, LatLng to) {
        double bearing = GetBearing(from, to);

        double adjBearing = Math.round(bearing / 3) * 3;
        while (adjBearing >= 120) {
            adjBearing -= 120;
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url;
        Bitmap image = null;

        try {
            url = new URL("http://www.google.com/intl/en_ALL/mapfiles/dir_" + String.valueOf((int) adjBearing) + ".png");
            try {
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (image != null) {

            float anchorX = 0.5f;
            float anchorY = 0.5f;

            int offsetX = 0;
            int offsetY = 0;


            //315 range -- 22.5 either side of 315
            if (bearing >= 292.5 && bearing < 335.5) {
                offsetX = 24;
                offsetY = 24;
            }
            //270 range
            else if (bearing >= 247.5 && bearing < 292.5) {
                offsetX = 24;
                offsetY = 12;
            }
            //225 range
            else if (bearing >= 202.5 && bearing < 247.5) {
                offsetX = 24;
                offsetY = 0;
            }
            //180 range
            else if (bearing >= 157.5 && bearing < 202.5) {
                offsetX = 12;
                offsetY = 0;
            }
            //135 range
            else if (bearing >= 112.5 && bearing < 157.5) {
                offsetX = 0;
                offsetY = 0;
            }
            //90 range
            else if (bearing >= 67.5 && bearing < 112.5) {
                offsetX = 0;
                offsetY = 12;
            }
            //45 range
            else if (bearing >= 22.5 && bearing < 67.5) {
                offsetX = 0;
                offsetY = 24;
            }
            //0 range - 335.5 - 22.5
            else {
                offsetX = 12;
                offsetY = 24;
            }

            Bitmap wideBmp;
            Canvas wideBmpCanvas;
            Rect src, dest;

            wideBmp = Bitmap.createBitmap(image.getWidth() * 2, image.getHeight() * 2, image.getConfig());

            wideBmpCanvas = new Canvas(wideBmp);

            src = new Rect(0, 0, image.getWidth(), image.getHeight());
            dest = new Rect(src);
            dest.offset(offsetX, offsetY);

            wideBmpCanvas.drawBitmap(image, src, dest, null);

            mMap.addMarker(new MarkerOptions()
                    .position(to)
                    .icon(BitmapDescriptorFactory.fromBitmap(wideBmp))
                    .anchor(anchorX, anchorY));
        }
    }


    private double GetBearing(LatLng from, LatLng to) {
        double lat1 = from.latitude * Math.PI / 180.0;
        double lon1 = from.longitude * Math.PI / 180.0;
        double lat2 = to.latitude * Math.PI / 180.0;
        double lon2 = to.longitude * Math.PI / 180.0;

        // Compute the angle.
        double angle = -Math.atan2(Math.sin(lon1 - lon2) * Math.cos(lat2), Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        if (angle < 0.0)
            angle += Math.PI * 2.0;

        // And convert result to degrees.
        angle = angle * degreesPerRadian;

        return angle;
    }


    class DirectionTask extends AsyncTask<String, Void, Data> {

        @Override
        protected Data doInBackground(String... strings) {
            String link = strings[0];
            try {
                URL url = new URL(link);
                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                Data googleData = new Gson().fromJson(inputStreamReader, Data.class);
                return googleData;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();

        }

        @Override
        protected void onPostExecute(Data data) {
            super.onPostExecute(data);
            dialog.dismiss();
            List<String> aaa = data.getListLocation();
            List<String> list = new ArrayList<>();
            for (int i = 0; i < aaa.size(); i++) {
                if (i % 120 == 0) {
                    list.add(aaa.get(i));
                }
            }


            for (int i = 0; i < list.size() - 1; i += 1) {


                LatLng from = new LatLng(Double.parseDouble(list.get(i).split("-")[1]), Double.parseDouble(list.get(i).split("-")[0]));
                LatLng to = new LatLng(Double.parseDouble(list.get(i + 1).split("-")[1]), Double.parseDouble(list.get(i + 1).split("-")[0]));


                PolylineOptions polylines = new PolylineOptions().
                        geodesic(true).
                        color(Color.RED).
                        width(2);
                polylines.add(from, to);
                mMap.addPolyline(polylines);


                DrawArrowHead(mMap, from, to);


            }


            mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(list.get(0).split("-")[1]), Double.parseDouble(list.get(0).split("-")[0]))).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(list.get(0).split("-")[1]), Double.parseDouble(list.get(0).split("-")[0]))));


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
