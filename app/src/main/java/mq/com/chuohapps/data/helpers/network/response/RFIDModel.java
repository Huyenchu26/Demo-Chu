package mq.com.chuohapps.data.helpers.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

public class RFIDModel {

    public String hour;

    public boolean isChecksum = false;

    @SerializedName("time")
    @Expose
    public String time;

    @SerializedName("rfid")
    @Expose
    public String rfid;

    public static Comparator<RFIDModel> VehicleHour = new Comparator<RFIDModel>() {
        @Override
        public int compare(RFIDModel vehicle, RFIDModel t1) {
            if (Integer.valueOf(t1.hour) < Integer.valueOf(vehicle.hour))
                return 1;
            else if (Integer.valueOf(t1.hour) == Integer.valueOf(vehicle.hour))
                return 0;
            else return -1;
        }
    };
}
