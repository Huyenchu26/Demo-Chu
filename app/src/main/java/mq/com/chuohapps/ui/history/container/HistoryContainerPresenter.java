package mq.com.chuohapps.ui.history.container;

import java.util.List;

import mq.com.chuohapps.data.DataCallBack;
import mq.com.chuohapps.data.helpers.network.response.Vehicle;
import mq.com.chuohapps.data.usecases.UseCaseHistoryContainer;
import mq.com.chuohapps.ui.xbase.BaseAppPresenter;

public class HistoryContainerPresenter extends BaseAppPresenter<HistoryContainerContract.View, UseCaseHistoryContainer>
        implements HistoryContainerContract.Presenter<HistoryContainerContract.View> {

    @Override
    public void getHistory(String imei, String startDate, String endDate) {
        if (getView() != null)
            getView().onStartGetHistory();
        getUseCase().getVehicle(imei, startDate, endDate, handleCallBack(new DataCallBack<List<Vehicle>>() {
            @Override
            public void onSuccess(List<Vehicle> response, String message) {
                getView().getHistorySuccess(response);
            }

            @Override
            public void onError(Throwable throwable, String message) {
                super.onError(throwable, message);
                getView().getHistoryError(message);
            }
        }));
    }
}
