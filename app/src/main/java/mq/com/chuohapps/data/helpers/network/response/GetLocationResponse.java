package mq.com.chuohapps.data.helpers.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetLocationResponse {
    @SerializedName("listLocation")
    @Expose
    public List<String> listLocation;

}
