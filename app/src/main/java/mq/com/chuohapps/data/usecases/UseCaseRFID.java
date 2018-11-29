package mq.com.chuohapps.data.usecases;

import android.support.annotation.NonNull;

import java.util.List;

import mq.com.chuohapps.data.DataCallBack;
import mq.com.chuohapps.data.helpers.network.response.RFIDModel;

public interface UseCaseRFID extends BaseUseCase {
    void getRFID(String imei, String startDate, String endDate,
                 @NonNull DataCallBack<List<RFIDModel>> callBack);
}
