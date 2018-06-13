package mq.com.chuohapps.data;

import android.support.annotation.NonNull;

import mq.com.chuohapps.data.helpers.local.PreferencesHelper;
import mq.com.chuohapps.data.helpers.network.ApiHelper;
import mq.com.chuohapps.data.helpers.network.response.Vehicle;
import mq.com.chuohapps.data.usecases.BaseUseCase;
import mq.com.chuohapps.data.usecases.UseCaseGetVehicle;


public class AppDataManager extends BaseDataManager implements
        UseCaseGetVehicle,
        BaseUseCase {

    public AppDataManager(ApiHelper apiHelper, PreferencesHelper preferencesHelper) {
        super(apiHelper, preferencesHelper);
    }

    @Override
    public void getVehicle(@NonNull DataCallBack<Vehicle> callBack) {
        apiHelper.getVehicle(handleCallBack(callBack));
    }
}
