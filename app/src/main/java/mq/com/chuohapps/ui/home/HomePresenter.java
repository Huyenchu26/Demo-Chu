package mq.com.chuohapps.ui.home;

import java.util.List;

import mq.com.chuohapps.data.DataCallBack;
import mq.com.chuohapps.data.helpers.network.response.GetImeiSavedResponse;
import mq.com.chuohapps.data.helpers.network.response.SaveImeiResponse;
import mq.com.chuohapps.data.helpers.network.response.Vehicle;
import mq.com.chuohapps.data.usecases.UseCaseGetVehicle;
import mq.com.chuohapps.ui.xbase.BaseAppPresenter;
import mq.com.chuohapps.utils.AppLogger;


public class HomePresenter extends BaseAppPresenter<HomeContract.View, UseCaseGetVehicle>
        implements HomeContract.Presenter<HomeContract.View> {

    @Override
    public void getVehicle() {
        if (getView() != null)
            getView().onStartGetVehicle();
        getUseCase().getVehicle(handleCallBack(new DataCallBack<List<Vehicle>>() {
            @Override
            public void onSuccess(List<Vehicle> response, String message) {
                getView().onGetVehicleSuccess(response);

            }

            @Override
            public void onError(Throwable throwable, String message) {
                super.onError(throwable, message);
                getView().onGetVehicleError(message);
            }
        }));
    }

    @Override
    public void saveImei(String imei, String numberCar) {
        if (getView() != null)
            getView().onStartSaveImei();
        getUseCase().saveImei(imei, numberCar, handleCallBack(new DataCallBack<SaveImeiResponse>() {
            @Override
            public void onSuccess(SaveImeiResponse response, String message) {
                getView().onSaveImeiSuccess(response);
            }

            @Override
            public void onError(Throwable throwable, String message) {
                super.onError(throwable, message);
                getView().onSaveImeiFailed(message);
            }
        }));
    }

    @Override
    public void getSavedImei() {
        if (getView() != null)
            getView().onStartGetImei();
        getUseCase().getImei(handleCallBack(new DataCallBack<GetImeiSavedResponse>() {
            @Override
            public void onSuccess(GetImeiSavedResponse response, String message) {
                getView().onGetImeiSuccess(response);
            }

            @Override
            public void onError(Throwable throwable, String message) {
                super.onError(throwable, message);
                getView().onGetImeiFailed(message);
            }
        }));
    }
}
