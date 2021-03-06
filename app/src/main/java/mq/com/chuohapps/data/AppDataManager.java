package mq.com.chuohapps.data;

import android.support.annotation.NonNull;

import java.util.List;

import mq.com.chuohapps.data.helpers.local.PreferencesHelper;
import mq.com.chuohapps.data.helpers.network.ApiHelper;
import mq.com.chuohapps.data.helpers.network.response.GetImeiSavedResponse;
import mq.com.chuohapps.data.helpers.network.response.GetLocationResponse;
import mq.com.chuohapps.data.helpers.network.response.RFIDModel;
import mq.com.chuohapps.data.helpers.network.response.SaveImeiResponse;
import mq.com.chuohapps.data.helpers.network.response.Vehicle;
import mq.com.chuohapps.data.usecases.BaseUseCase;
import mq.com.chuohapps.data.usecases.UseCaseGetListLocation;
import mq.com.chuohapps.data.usecases.UseCaseGetVehicle;
import mq.com.chuohapps.data.usecases.UseCaseHistoryContainer;
import mq.com.chuohapps.data.usecases.UseCaseRFID;


public class AppDataManager extends BaseDataManager implements
        UseCaseGetVehicle,
        UseCaseHistoryContainer,
        UseCaseGetListLocation,
        UseCaseRFID,
        BaseUseCase {

    public AppDataManager(ApiHelper apiHelper, PreferencesHelper preferencesHelper) {
        super(apiHelper, preferencesHelper);
    }

    @Override
    public void getVehicle(@NonNull DataCallBack<List<Vehicle>> callBack) {
        apiHelper.getVehicle(handleCallBack(callBack));
    }

    @Override
    public void getVehicle(String imei, String startDate, String endDate, @NonNull DataCallBack<List<Vehicle>> callBack) {
        apiHelper.getHistory(imei, startDate, endDate, handleCallBack(callBack));
    }

    @Override
    public void getListLocation(String imei, String startDate, String endDate, boolean bool,
                                @NonNull DataCallBack<GetLocationResponse> callBack) {
        apiHelper.getLocation(imei, startDate, endDate, bool, handleCallBack(callBack));
    }

    @Override
    public void saveImei(String imei, String numberCar, @NonNull DataCallBack<SaveImeiResponse> callBack) {
        apiHelper.saveImei(imei, numberCar, handleCallBack(callBack));
    }

    @Override
    public void getImei(@NonNull DataCallBack<GetImeiSavedResponse> callBack) {
        apiHelper.getSavedImei(handleCallBack(callBack));
    }

    @Override
    public void getRFID(String imei, String startDate, String endDate, @NonNull DataCallBack<List<RFIDModel>> callBack) {
        apiHelper.getRFID(imei, startDate, endDate, handleCallBack(callBack));
    }
}
