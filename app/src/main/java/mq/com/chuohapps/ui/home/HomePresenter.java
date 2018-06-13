package mq.com.chuohapps.ui.home;

import mq.com.chuohapps.data.usecases.UseCaseGetVehicle;
import mq.com.chuohapps.ui.xbase.BaseAppPresenter;


/**
 * Created by nguyen.dang.tho on 10/24/2017.
 */

public class HomePresenter extends BaseAppPresenter<HomeContract.View, UseCaseGetVehicle>
        implements HomeContract.Presenter<HomeContract.View> {

    @Override
    public void getVehicle() {

    }
}
