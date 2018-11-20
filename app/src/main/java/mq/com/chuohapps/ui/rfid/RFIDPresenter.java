package mq.com.chuohapps.ui.rfid;

import java.util.List;

import mq.com.chuohapps.data.DataCallBack;
import mq.com.chuohapps.data.helpers.network.response.Vehicle;
import mq.com.chuohapps.data.usecases.UseCaseHistoryContainer;
import mq.com.chuohapps.data.usecases.UseCaseRFID;
import mq.com.chuohapps.ui.xbase.BaseAppPresenter;

public class RFIDPresenter extends BaseAppPresenter<RFIDContract.View, UseCaseHistoryContainer>
        implements RFIDContract.Presenter<RFIDContract.View>{
    @Override
    public void getRFID(String imei, String startDate, String endDate) {
        if (getView() != null) getView().onStartGetRFID();
        getUseCase().getVehicle(imei, startDate, endDate, handleCallBack(new DataCallBack<List<Vehicle>>() {
            @Override
            public void onSuccess(List<Vehicle> response, String message) {
                getView().onGetRFIDSuccess(response);
            }

            @Override
            public void onError(Throwable throwable, String message) {
                super.onError(throwable, message);
                getView().onGetFRIDFailed(message);
            }
        }));

    }
}
