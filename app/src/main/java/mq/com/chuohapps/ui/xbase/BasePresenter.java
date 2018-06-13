package mq.com.chuohapps.ui.xbase;

import android.support.annotation.NonNull;


public interface BasePresenter<V extends BaseView> {

    void onAttachView(@NonNull V view);

    void onDetachView();

    void onAttachUseCase();

    void onDetachUseCase();

}
