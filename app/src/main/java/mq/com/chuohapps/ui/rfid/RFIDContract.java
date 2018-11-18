package mq.com.chuohapps.ui.rfid;

import mq.com.chuohapps.ui.xbase.BaseContract;
import mq.com.chuohapps.ui.xbase.BasePresenter;
import mq.com.chuohapps.ui.xbase.BaseView;

public class RFIDContract extends BaseContract{
    public interface View extends BaseView {
        void onStartGetRFID();

        void onGetRFIDSuccess();

        void onGetFRIDFailed(String message);
    }

    public interface Presenter<V extends View> extends BasePresenter<V> {
        void getRFID();
    }
}
