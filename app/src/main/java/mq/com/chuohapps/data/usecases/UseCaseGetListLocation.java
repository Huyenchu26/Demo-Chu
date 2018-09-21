package mq.com.chuohapps.data.usecases;

import android.support.annotation.NonNull;
import com.google.android.gms.maps.model.LatLng;
import java.util.List;
import mq.com.chuohapps.data.DataCallBack;

public interface UseCaseGetListLocation extends BaseUseCase {
    void getListLocation(String imei,
                    String startDate,
                    String endDate,
                    @NonNull DataCallBack<List<LatLng>> callBack);
}
