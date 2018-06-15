package mq.com.chuohapps.data.usecases;

import android.support.annotation.NonNull;

import java.util.List;

import mq.com.chuohapps.data.DataCallBack;
import mq.com.chuohapps.data.helpers.network.response.Vehicle;

/**
 * Created by chu.thi.ngoc.huyen on 2/8/2018.
 */

public interface UseCaseHistoryContainer extends BaseUseCase {
    void getVehicle(String imei,
                    String startDate,
                    String endDate,
                    @NonNull DataCallBack<List<Vehicle>> callBack);
}
