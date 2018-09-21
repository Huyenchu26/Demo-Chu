package mq.com.chuohapps.ui.maps;

import mq.com.chuohapps.data.usecases.UseCaseGetListLocation;
import mq.com.chuohapps.ui.xbase.BaseAppPresenter;

public class MapsPresenter extends BaseAppPresenter<MapsConstract.View, UseCaseGetListLocation>
        implements MapsConstract.Presenter<MapsConstract.View> {
    @Override
    public void getListLocation(String imei, String startDate, String endDate) {

    }
}
