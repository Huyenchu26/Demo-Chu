package mq.com.chuohapps.data.helpers.network;

import android.support.annotation.NonNull;

import mq.com.chuohapps.data.helpers.network.base.BaseApiCallBack;
import mq.com.chuohapps.data.helpers.network.base.BaseResponse;
import retrofit2.Response;


/**
 * Created by nguyen.dang.tho on 9/1/2017.
 */

public abstract class ApiCallBack<R extends BaseResponse> extends BaseApiCallBack<R> {

    private static final int RESPONSE_CODE_OK = 1000; //=> 'OK',
    private static final int RESPONSE_CODE_FAILED = 9000;// => 'Fail',
    private static final int RESPONSE_CODE_MISSING_PARAM = 9990;//=> 'Not enought required parameters.',
    private static final int RESPONSE_CODE_INVALID_PARAM = 9991;//=> 'Parameter is invalid',
    private static final int RESPONSE_CODE_CONNECT_DB_ERROR = 9992;// => 'Can not connect to DB',
    private static final int RESPONSE_CODE_INVALID_METHOD = 9997;// => 'Method is invalid',
    private static final int RESPONSE_CODE_ERROR = 9999; //=> 'Error',
    private static final int RESPONSE_CODE_ERROR_UNKNOWN = 1001;// => 'Unknown error.',

    private static final int RESPONSE_CODE_TOKEN_BLACK_LIST = 9993;// => 'Token in blacklisted',
    private static final int RESPONSE_CODE_TOKEN_EXPIRED = 9994;//=> 'Token is expired',
    private static final int RESPONSE_CODE_LOGIN_ERROR = 9995;// => 'Cannot login',
    private static final int RESPONSE_CODE_INVALID_USER = 1002;//=> 'User not found',
    private static final int RESPONSE_CODE_UNAUTHORIZED = 1006;// => 'Unauthorized',
    private static final int RESPONSE_CODE_TOKEN_INVALID = 9998;// => 'Token invalid',

    @Override
    public void handleSuccessResponse(@NonNull Response<R> response) {

    }

}
