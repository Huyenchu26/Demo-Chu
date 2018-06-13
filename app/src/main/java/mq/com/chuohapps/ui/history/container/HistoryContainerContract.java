package mq.com.chuohapps.ui.history.container;

import mq.com.chuohapps.ui.xbase.BaseContract;
import mq.com.chuohapps.ui.xbase.BasePresenter;
import mq.com.chuohapps.ui.xbase.BaseView;

public class HistoryContainerContract extends BaseContract {
    public interface Presenter<V extends View> extends BasePresenter<V> {

    }

    interface View extends BaseView {

    }
}
