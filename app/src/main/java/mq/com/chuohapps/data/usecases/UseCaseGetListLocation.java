package mq.com.chuohapps.data.usecases;

import android.support.annotation.NonNull;
import com.google.android.gms.maps.model.LatLng;
import java.util.List;
import mq.com.chuohapps.data.DataCallBack;
import mq.com.chuohapps.data.helpers.network.response.GetLocationResponse;
import mq.com.chuohapps.data.helpers.network.response.Vehicle;

public interface UseCaseGetListLocation extends BaseUseCase {
    void getListLocation(String imei,
                    String startDate,
                    String endDate,
                    boolean bool,
                    @NonNull DataCallBack<GetLocationResponse> callBack);
}
