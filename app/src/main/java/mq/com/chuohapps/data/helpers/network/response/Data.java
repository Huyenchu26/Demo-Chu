
package mq.com.chuohapps.data.helpers.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("imei")
    @Expose
    private String imei;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("reserve_0")
    @Expose
    private String reserve0;
    @SerializedName("reserve_1")
    @Expose
    private String reserve1;
    @SerializedName("reserve_2")
    @Expose
    private String reserve2;
    @SerializedName("sos")
    @Expose
    private String sos;
    @SerializedName("trunk")
    @Expose
    private String trunk;
    @SerializedName("engine")
    @Expose
    private String engine;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("gps")
    @Expose
    private String gps;
    @SerializedName("front_cam")
    @Expose
    private String frontCam;
    @SerializedName("back_cam")
    @Expose
    private String backCam;
    @SerializedName("rfid_list")
    @Expose
    private String rfidList;
    @SerializedName("reserve_3")
    @Expose
    private String reserve3;
    @SerializedName("pos_status")
    @Expose
    private String posStatus;
    @SerializedName("firmware")
    @Expose
    private String firmware;
    @SerializedName("cpu_time")
    @Expose
    private String cpuTime;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getReserve0() {
        return reserve0;
    }

    public void setReserve0(String reserve0) {
        this.reserve0 = reserve0;
    }

    public String getReserve1() {
        return reserve1;
    }

    public void setReserve1(String reserve1) {
        this.reserve1 = reserve1;
    }

    public String getReserve2() {
        return reserve2;
    }

    public void setReserve2(String reserve2) {
        this.reserve2 = reserve2;
    }

    public String getSos() {
        return sos;
    }

    public void setSos(String sos) {
        this.sos = sos;
    }

    public String getTrunk() {
        return trunk;
    }

    public void setTrunk(String trunk) {
        this.trunk = trunk;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getFrontCam() {
        return frontCam;
    }

    public void setFrontCam(String frontCam) {
        this.frontCam = frontCam;
    }

    public String getBackCam() {
        return backCam;
    }

    public void setBackCam(String backCam) {
        this.backCam = backCam;
    }

    public String getRfidList() {
        return rfidList;
    }

    public void setRfidList(String rfidList) {
        this.rfidList = rfidList;
    }

    public String getReserve3() {
        return reserve3;
    }

    public void setReserve3(String reserve3) {
        this.reserve3 = reserve3;
    }

    public String getPosStatus() {
        return posStatus;
    }

    public void setPosStatus(String posStatus) {
        this.posStatus = posStatus;
    }

    public String getFirmware() {
        return firmware;
    }

    public void setFirmware(String firmware) {
        this.firmware = firmware;
    }

    public String getCpuTime() {
        return cpuTime;
    }

    public void setCpuTime(String cpuTime) {
        this.cpuTime = cpuTime;
    }

}
