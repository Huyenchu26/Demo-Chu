package mq.com.chuohapps.ui.rfid;

import java.util.List;

import mq.com.chuohapps.data.helpers.network.response.Vehicle;
import mq.com.chuohapps.ui.xbase.BaseContract;
import mq.com.chuohapps.ui.xbase.BasePresenter;
import mq.com.chuohapps.ui.xbase.BaseView;

public class RFIDContract extends BaseContract{
    public interface View extends BaseView {
        void onStartGetRFID();

        void onGetRFIDSuccess(List<Vehicle> response);

        void onGetFRIDFailed(String message);
    }

    public interface Presenter<V extends View> extends BasePresenter<V> {
        void getRFID(String imei, String startDate, String endDate);
    }
}
