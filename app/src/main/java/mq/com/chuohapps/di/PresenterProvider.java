package mq.com.chuohapps.di;

import mq.com.chuohapps.ui.history.CPUTime.CPUContract;
import mq.com.chuohapps.ui.history.CPUTime.CPUPresenter;
import mq.com.chuohapps.ui.history.Trunk.TrunkContract;
import mq.com.chuohapps.ui.history.Trunk.TrunkPresenter;
import mq.com.chuohapps.ui.history.container.HistoryContainerContract;
import mq.com.chuohapps.ui.history.container.HistoryContainerPresenter;
import mq.com.chuohapps.ui.home.HomeContract;
import mq.com.chuohapps.ui.home.HomePresenter;
import mq.com.chuohapps.ui.maps.MapsConstract;
import mq.com.chuohapps.ui.maps.MapsPresenter;
import mq.com.chuohapps.ui.xbase.BasePresenter;
import mq.com.chuohapps.ui.xbase.container.ContainerContract;
import mq.com.chuohapps.ui.xbase.container.ContainerPresenter;

public class PresenterProvider {
    private static boolean matched(Class<?> classTypeSource, Class<?> classTypeDestination) {
        return classTypeSource.getCanonicalName().equals(classTypeDestination.getCanonicalName());
    }

    public static <P extends BasePresenter> Object provide(Class<P> classType) {
        if (matched(classType, ContainerContract.Presenter.class)) {
            return new ContainerPresenter();
        } if (matched(classType, HomeContract.Presenter.class)) {
            return new HomePresenter();
        } if (matched(classType, HistoryContainerContract.Presenter.class)) {
            return new HistoryContainerPresenter();
        } if (matched(classType, MapsConstract.Presenter.class)) {
            return new MapsPresenter();
        } if (matched(classType, CPUContract.Presenter.class)) {
            return new CPUPresenter();
        } if (matched(classType, TrunkContract.Presenter.class)) {
            return new TrunkPresenter();
        }

        throw new IllegalArgumentException("Add [" + classType.getCanonicalName() + "]" + " to Presenter Provider!!!");
    }
}
