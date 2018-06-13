package mq.com.chuohapps.data.helpers.network.response;

import java.util.List;

public class VehicleParent {

    public VehicleParent(String imei, String date, String time) {
        this.imei = imei;
        this.date = date;
        this.time = time;
    }

    public String imei;
    public String date;
    public String time;

    private List<Object> childList;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
