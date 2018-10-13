package mq.com.chuohapps.data.usecases;

import android.support.annotation.NonNull;

import java.util.List;

import mq.com.chuohapps.data.DataCallBack;
import mq.com.chuohapps.data.helpers.network.response.SaveImeiResponse;
import mq.com.chuohapps.data.helpers.network.response.Vehicle;

public interface UseCaseGetVehicle extends BaseUseCase{
    void getVehicle(@NonNull DataCallBack<List<Vehicle>> callBack);

    void saveImei(String imei, String numberCar,
                  @NonNull DataCallBack<SaveImeiResponse> callBack);
}
