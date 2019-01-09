package mq.com.chuohapps.ui.demomaps;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import mq.com.chuohapps.R;

public class MapsActivityDemo extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading");
        dialog.setCanceledOnTouchOutside(false);

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

    public static ProgressDialog dialog;


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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //    LatLng sydney = new LatLng(-34, 151);
        String link = "http://navistardev.cloudapp.net:5005/api/Values?imei=011712120219351&startDate=2019/01/06%2020:29:25&endDate=2019/01/07%2007:29:25";
        DirectionTask directionTask = new DirectionTask();
        directionTask.execute(link);

    }
}
