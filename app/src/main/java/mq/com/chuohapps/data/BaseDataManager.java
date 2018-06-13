package mq.com.chuohapps.data;

import mq.com.chuohapps.AppConfigs;
import mq.com.chuohapps.data.helpers.local.PreferencesHelper;
import mq.com.chuohapps.data.helpers.network.ApiHelper;
import mq.com.chuohapps.data.helpers.network.base.BaseResponse;
import mq.com.chuohapps.data.usecases.BaseUseCase;


public abstract class BaseDataManager implements DataManager, BaseUseCase {

    protected ApiHelper apiHelper;
    protected PreferencesHelper preferencesHelper;
    protected String loginToken = AppConfigs.EMPTY;

    public BaseDataManager(ApiHelper apiHelper, PreferencesHelper preferencesHelper) {
        this.apiHelper = apiHelper;
        this.preferencesHelper = preferencesHelper;
    }

    @Override
    public void restart() {
        loginToken = AppConfigs.EMPTY;
        preferencesHelper.clear();
    }

    protected <R extends BaseResponse> DataCallBack<R> handleCache(final Class<?> classType, final DataCallBack<R> callBack) {
        return new DataCallBack<R>() {
            @Override
            public void onSuccess(R response, String message) {
                callBack.onSuccess(response, message);
                preferencesHelper.saveCache(response, classType);
            }

            @Override
            public void onError(Throwable throwable, String message) {
                super.onError(throwable, message);
                callBack.onError(throwable, message);
            }

            @Override
            public void onNeedLogout() {
                super.onNeedLogout();
                callBack.onNeedLogout();
            }
        };
    }

    protected <R extends BaseResponse> DataCallBack<R> handleCallBack(final DataCallBack<R> callBack) {
        return new DataCallBack<R>() {
            @Override
            public void onSuccess(R response, String message) {
                if (callBack != null)
                    callBack.onSuccess(response, message);
            }

            @Override
            public void onError(Throwable throwable, String message) {
                if (callBack != null)
                    callBack.onError(throwable, message);
            }

            @Override
            public void onNeedLogout() {
                if (callBack != null)
                    callBack.onNeedLogout();
            }
        };
    }
}
