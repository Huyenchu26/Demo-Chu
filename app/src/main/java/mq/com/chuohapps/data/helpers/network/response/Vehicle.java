package mq.com.chuohapps.data.helpers.network.response;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

import mq.com.chuohapps.utils.data.DateUtils;

public class Vehicle {

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
    @SerializedName("SOS")
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
    @SerializedName("size")
    @Expose
    public String size;
    @SerializedName("line_all")
    @Expose
    public String lineAll;

    public static Comparator<Vehicle> VehicleImei = new Comparator<Vehicle>() {
        @Override
        public int compare(Vehicle vehicle, Vehicle t1) {
            if (Double.valueOf(t1.imei) > Double.valueOf(vehicle.imei))
                return -1;
            else if (Double.valueOf(t1.imei) == Double.valueOf(vehicle.imei))
                return 0;
            else return 1;
        }
    };

    public static Comparator<Vehicle> VehicleDate = new Comparator<Vehicle>() {
        @Override
        public int compare(Vehicle vehicle, Vehicle t1) {
            if (DateUtils.getSecond(t1.dateTime) < DateUtils.getSecond(vehicle.dateTime))
                return -1;
            else if (DateUtils.getSecond(t1.dateTime) == DateUtils.getSecond(vehicle.dateTime))
                return 0;
            else return 1;
        }
    };

    public static Comparator<Vehicle> VehicleSize = new Comparator<Vehicle>() {
        @Override
        public int compare(Vehicle vehicle, Vehicle t1) {
            if (Long.valueOf(t1.size) < Long.valueOf(vehicle.size))
                return -1;
            else if (Long.valueOf(t1.size) == Long.valueOf(vehicle.size))
                return 0;
            else return 1;
        }
    };

}