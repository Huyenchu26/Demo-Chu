package mq.com.chuohapps.data.helpers.network.response;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Vehicle implements Comparable<Vehicle>{

    public Vehicle(Data data) {
        this.data = data;
    }

    @SerializedName("timestamp_recv")
    @Expose
    public TimestampRecv timestampRecv;
    @SerializedName("data")
    @Expose
    public Data data;

    public class TimestampRecv {

        @SerializedName("ukn_dtl_1")
        @Expose
        public String uknDtl1;
        @SerializedName("rec_timestamp")
        @Expose
        public String recTimestamp;
        @SerializedName("ukn_dtl_2")
        @Expose
        public String uknDtl2;
    }

    public static class Data {

        public Data(String imei, String dateTime, String longitude, String latitude,
                    String reserve0, String reserve1, String reserve2, String sos,
                    String trunk, String engine, String status, String gps, String frontCam,
                    String backCam, String rfidList, String reserve3, String posStatus,
                    String firmware, String cpuTime) {
            this.imei = imei;
            this.dateTime = dateTime;
            this.longitude = longitude;
            this.latitude = latitude;
            this.reserve0 = reserve0;
            this.reserve1 = reserve1;
            this.reserve2 = reserve2;
            this.sos = sos;
            this.trunk = trunk;
            this.engine = engine;
            this.status = status;
            this.gps = gps;
            this.frontCam = frontCam;
            this.backCam = backCam;
            this.rfidList = rfidList;
            this.reserve3 = reserve3;
            this.posStatus = posStatus;
            this.firmware = firmware;
            this.cpuTime = cpuTime;
        }

        @SerializedName("imei")
        @Expose
        public String imei;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("longitude")
        @Expose
        public String longitude;
        @SerializedName("latitude")
        @Expose
        public String latitude;
        @SerializedName("reserve_0")
        @Expose
        public String reserve0;
        @SerializedName("reserve_1")
        @Expose
        public String reserve1;
        @SerializedName("reserve_2")
        @Expose
        public String reserve2;
        @SerializedName("sos")
        @Expose
        public String sos;
        @SerializedName("trunk")
        @Expose
        public String trunk;
        @SerializedName("engine")
        @Expose
        public String engine;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("gps")
        @Expose
        public String gps;
        @SerializedName("front_cam")
        @Expose
        public String frontCam;
        @SerializedName("back_cam")
        @Expose
        public String backCam;
        @SerializedName("rfid_list")
        @Expose
        public String rfidList;
        @SerializedName("reserve_3")
        @Expose
        public String reserve3;
        @SerializedName("pos_status")
        @Expose
        public String posStatus;
        @SerializedName("firmware")
        @Expose
        public String firmware;
        @SerializedName("cpu_time")
        @Expose
        public String cpuTime;
    }

    @Override
    public int compareTo(@NonNull Vehicle vehicle) {
        if (Double.valueOf(this.data.imei) > Double.valueOf(vehicle.data.imei))
            return 1;
        else if (Double.valueOf(this.data.imei) == Double.valueOf(vehicle.data.imei))
            return 0;
        else return -1;
    }
}