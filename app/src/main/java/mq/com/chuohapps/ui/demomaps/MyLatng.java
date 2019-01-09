package mq.com.chuohapps.ui.demomaps;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by AMBE on 8/1/2019 at 11:47 AM.
 */
public class MyLatng {

    private LatLng from;
    private LatLng to;

    public MyLatng(LatLng from, LatLng to) {
        this.from = from;
        this.to = to;
    }

    public MyLatng() {
    }

    public LatLng getFrom() {
        return from;
    }

    public void setFrom(LatLng from) {
        this.from = from;
    }

    public LatLng getTo() {
        return to;
    }

    public void setTo(LatLng to) {
        this.to = to;
    }
}
