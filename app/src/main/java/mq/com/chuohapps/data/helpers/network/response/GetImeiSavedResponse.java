package mq.com.chuohapps.data.helpers.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetImeiSavedResponse {
    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("code")
    @Expose
    public int code;

    @SerializedName("result")
    @Expose
    public List<String> result;

    @SerializedName("listnumber")
    @Expose
    public List<String> listnumber;
}
