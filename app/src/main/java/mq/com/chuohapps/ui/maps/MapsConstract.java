package mq.com.chuohapps.ui.maps;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import mq.com.chuohapps.ui.xbase.BaseContract;
import mq.com.chuohapps.ui.xbase.BasePresenter;
import mq.com.chuohapps.ui.xbase.BaseView;

public class MapsConstract extends BaseContract{

    public interface Presenter<V extends View> extends BasePresenter<V> {
        void getListLocation(String imei, String startDate, String endDate);
    }

    interface View extends BaseView {
        void onStartGetListLocation();

        void onGetListLocationSuccess(List<LatLng> latLngs);

        void onGetListLocationError(String message);

    }
}
