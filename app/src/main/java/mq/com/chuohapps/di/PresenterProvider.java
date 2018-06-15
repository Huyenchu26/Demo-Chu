package mq.com.chuohapps.di;

import mq.com.chuohapps.ui.history.container.HistoryContainerContract;
import mq.com.chuohapps.ui.history.container.HistoryContainerPresenter;
import mq.com.chuohapps.ui.home.HomeContract;
import mq.com.chuohapps.ui.home.HomePresenter;
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
        }
        throw new IllegalArgumentException("Add [" + classType.getCanonicalName() + "]" + " to Presenter Provider!!!");
    }
}
