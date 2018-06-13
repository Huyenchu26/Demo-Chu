package mq.com.chuohapps.data.usecases;

import android.support.annotation.NonNull;

import mq.com.chuohapps.data.DataCallBack;
import mq.com.chuohapps.data.helpers.network.response.Vehicle;

public interface UseCaseGetVehicle extends BaseUseCase{
    void getVehicle(@NonNull DataCallBack<Vehicle> callBack);
}
