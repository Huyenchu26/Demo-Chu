package mq.com.chuohapps.ui.home;

import java.util.List;

import mq.com.chuohapps.data.helpers.network.response.GetImeiSavedResponse;
import mq.com.chuohapps.data.helpers.network.response.SaveImeiResponse;
import mq.com.chuohapps.data.helpers.network.response.Vehicle;
import mq.com.chuohapps.ui.xbase.BaseContract;
import mq.com.chuohapps.ui.xbase.BasePresenter;
import mq.com.chuohapps.ui.xbase.BaseView;


public class HomeContract extends BaseContract {

    public interface Presenter<V extends View> extends BasePresenter<V> {
        void getVehicle();

        void saveImei(String imei, String numberCar);

        void getSavedImei();
    }

    interface View extends BaseView {
        void onStartGetVehicle();

        void onGetVehicleSuccess(List<Vehicle> vehicleList);

        void onGetVehicleError(String message);

        void onStartSaveImei();

        void onSaveImeiSuccess(SaveImeiResponse response);

        void onSaveImeiFailed(String message);

        void onStartGetImei();

        void onGetImeiSuccess(GetImeiSavedResponse response);

        void onGetImeiFailed(String message);
    }
}
