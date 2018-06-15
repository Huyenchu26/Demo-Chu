package mq.com.chuohapps.ui.history.container;

import java.util.List;

import mq.com.chuohapps.data.helpers.network.response.Vehicle;
import mq.com.chuohapps.ui.xbase.BaseContract;
import mq.com.chuohapps.ui.xbase.BasePresenter;
import mq.com.chuohapps.ui.xbase.BaseView;

public class HistoryContainerContract extends BaseContract {
    public interface Presenter<V extends View> extends BasePresenter<V> {
        void getHistory(String imei, String startDate, String endDate);
    }

    interface View extends BaseView {
        void onStartGetHistory();

        void getHistorySuccess(List<Vehicle> vehicleList);

        void getHistoryError(String message);
    }
}
