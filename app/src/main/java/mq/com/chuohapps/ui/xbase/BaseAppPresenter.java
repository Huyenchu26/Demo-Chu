package mq.com.chuohapps.ui.xbase;

import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

import mq.com.chuohapps.data.DataCallBack;
import mq.com.chuohapps.data.usecases.BaseUseCase;
import mq.com.chuohapps.di.DataProvider;



public abstract class BaseAppPresenter<V extends BaseView, U extends BaseUseCase> implements BasePresenter<V> {


    protected boolean isLoadingData = false;
    private U useCase;
    private WeakReference<V> view;

    /**
     * Get presenter's view
     *
     * @return view
     */
    public V getView() {
        return view == null ? null : view.get();
    }

    /**
     * Get presenter's use case
     *
     * @return use case
     */
    public U getUseCase() {
        return useCase;
    }

    @Override
    public void onAttachUseCase() {
        this.useCase = (U) DataProvider.provideDataManager();
    }

    @Override
    public void onDetachUseCase() {
        this.useCase = null;
    }

    @Override
    public void onAttachView(@NonNull V view) {
        this.view = new WeakReference<>(view);
    }

    @Override
    public void onDetachView() {

        this.view = null;
    }

    protected void restart() {
        getUseCase().restart();
        if (getView() != null)
            getView().restart();

    }

    protected <R> DataCallBack<R> handleCallBack(final DataCallBack<R> callBack) {
        return new DataCallBack<R>() {
            @Override
            public void onSuccess(R response, String message) {
                isLoadingData = false;
                if (getView() == null) return;
                callBack.onSuccess(response, message);

            }

            @Override
            public void onError(Throwable throwable, String message) {
                isLoadingData = false;
                if (getView() == null) return;
                callBack.onError(throwable, message);
            }

            @Override
            public void onNeedLogout() {
                isLoadingData = false;
                restart();
            }
        };
    }
}
