package mq.com.chuohapps.ui.maps;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import mq.com.chuohapps.data.DataCallBack;
import mq.com.chuohapps.data.helpers.network.response.GetLocationResponse;
import mq.com.chuohapps.data.helpers.network.response.Vehicle;
import mq.com.chuohapps.data.usecases.UseCaseGetListLocation;
import mq.com.chuohapps.ui.xbase.BaseAppPresenter;

public class MapsPresenter extends BaseAppPresenter<MapsConstract.View, UseCaseGetListLocation>
        implements MapsConstract.Presenter<MapsConstract.View> {
    @Override
    public void getListLocation(String imei, String startDate, String endDate) {
        if (getView() != null)
            getView().onStartGetListLocation();
        getUseCase().getListLocation(imei, startDate, endDate, handleCallBack(new DataCallBack<GetLocationResponse>() {
            @Override
            public void onSuccess(GetLocationResponse response, String message) {
                getView().onGetListLocationSuccess(response.listLocation);
            }

            @Override
            public void onError(Throwable throwable, String message) {
                super.onError(throwable, message);
                getView().onGetListLocationError(message);
            }
        }));
    }
}
