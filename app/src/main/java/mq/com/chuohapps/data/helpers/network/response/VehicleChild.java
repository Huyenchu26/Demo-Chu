package mq.com.chuohapps.data.helpers.network.response;

import android.os.Parcelable;

import java.util.List;

/**
 * Created by Admin on 29/3/2018.
 */

public abstract class VehicleChild implements Parcelable {

    public VehicleChild() {
    }

    public double longitude;
    public double latitude;
    public boolean sos;
    public boolean trunk;
    public boolean engine;
    public boolean gps;
    public int frontCamera;
    public int behindCamera;
    public String locate;

    public List<String> rfid;

    public List<String> getRfid() {
        return rfid;
    }

    public void setRfid(List<String> rfid) {
        this.rfid = rfid;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public boolean isSos() {
        return sos;
    }

    public boolean isTrunk() {
        return trunk;
    }

    public boolean isEngine() {
        return engine;
    }

    public boolean isGps() {
        return gps;
    }

    public int getFrontCamera() {
        return frontCamera;
    }

    public int getBehindCamera() {
        return behindCamera;
    }

    public String getLocate() {
        return locate;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setSos(boolean sos) {
        this.sos = sos;
    }

    public void setTrunk(boolean trunk) {
        this.trunk = trunk;
    }

    public void setEngine(boolean engine) {
        this.engine = engine;
    }

    public void setGps(boolean gps) {
        this.gps = gps;
    }

    public void setFrontCamera(int frontCamera) {
        this.frontCamera = frontCamera;
    }

    public void setBehindCamera(int behindCamera) {
        this.behindCamera = behindCamera;
    }

    public void setLocate(String locate) {
        this.locate = locate;
    }
}
