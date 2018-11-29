package mq.com.chuohapps.data.helpers.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RFIDModel {
    @SerializedName("time")
    @Expose
    public String time;

    @SerializedName("rfid")
    @Expose
    public int rfid;
}
