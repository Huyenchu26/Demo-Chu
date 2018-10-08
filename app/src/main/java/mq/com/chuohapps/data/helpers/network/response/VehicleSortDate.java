package mq.com.chuohapps.data.helpers.network.response;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import mq.com.chuohapps.utils.data.DateUtils;

public class VehicleSortDate implements Comparable<Vehicle>{

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
        if (DateUtils.getSecond(this.data.dateTime) < DateUtils.getSecond(vehicle.dateTime))
            return -1;
        else if (DateUtils.getSecond(this.data.dateTime) == DateUtils.getSecond(vehicle.dateTime))
            return 0;
        else return 1;
    }
}