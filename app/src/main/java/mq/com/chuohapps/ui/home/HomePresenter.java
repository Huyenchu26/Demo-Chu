package mq.com.chuohapps.ui.home;

import mq.com.chuohapps.data.DataCallBack;
import mq.com.chuohapps.data.helpers.network.response.Vehicle;
import mq.com.chuohapps.data.usecases.UseCaseGetVehicle;
import mq.com.chuohapps.ui.xbase.BaseAppPresenter;


public class HomePresenter extends BaseAppPresenter<HomeContract.View, UseCaseGetVehicle>
        implements HomeContract.Presenter<HomeContract.View> {

    @Override
    public void getVehicle() {
        if (getView() != null)
            getView().onStartGetVehicle();
        getUseCase().getVehicle(handleCallBack(new DataCallBack<Vehicle>() {
            @Override
            public void onSuccess(Vehicle response, String message) {
                getView().onGetVehicleSuccess(response);
            }

            @Override
            public void onError(Throwable throwable, String message) {
                super.onError(throwable, message);
                getView().onGetVehicleError(message);
            }
        }));
    }
}
