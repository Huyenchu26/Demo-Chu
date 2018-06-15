package mq.com.chuohapps.data.helpers.network;

import android.support.annotation.NonNull;

import java.util.List;

import mq.com.chuohapps.data.DataCallBack;
import mq.com.chuohapps.data.helpers.network.base.BaseApiHelper;
import mq.com.chuohapps.data.helpers.network.response.Vehicle;
import mq.com.chuohapps.utils.AppLogger;
import okhttp3.OkHttpClient;


public class ApiHelper extends BaseApiHelper {

    public ApiHelper(OkHttpClient client) {
        super(client);
    }

    public void getVehicle(@NonNull DataCallBack<List<Vehicle>> callBack) {
        AppLogger.error("ApiHelper: ");
        getClient().loadVehicles().enqueue(handle(callBack));
    }

    public void getHistory(String imei, String startDate, String endDate,
                           @NonNull DataCallBack<List<Vehicle>> callBack) {
        getClient().loadHistory(imei, startDate, endDate).enqueue(handle(callBack));
    }
}
