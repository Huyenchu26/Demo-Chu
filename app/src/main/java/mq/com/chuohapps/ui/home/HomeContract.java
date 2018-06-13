package mq.com.chuohapps.ui.home;

import java.util.List;

import mq.com.chuohapps.data.helpers.network.response.Vehicle;
import mq.com.chuohapps.ui.xbase.BaseContract;
import mq.com.chuohapps.ui.xbase.BasePresenter;
import mq.com.chuohapps.ui.xbase.BaseView;


/**
 * Created by nguyen.dang.tho on 10/24/2017.
 */

public class HomeContract extends BaseContract {

    public interface Presenter<V extends View> extends BasePresenter<V> {
        void getVehicle();
    }

    interface View extends BaseView {
        void onStartGetVehicle();

        void onGetVehicleSuccess(List<Vehicle> vehicleList);

        void onGetVehicleError(String message);

    }
}
