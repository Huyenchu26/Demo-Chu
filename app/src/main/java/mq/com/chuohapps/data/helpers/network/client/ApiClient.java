package mq.com.chuohapps.data.helpers.network.client;

import java.util.List;

import mq.com.chuohapps.data.helpers.network.response.Vehicle;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiClient {
    String HEADER_CONTENT_TYPE = "Content-Type";
    String HEADER_CONTENT_TYPE_VALUE = "application/json";

    String HEADER_CONTENT_LENGTH = "Content-Length";
    String HEADER_CONTENT_LENGTH_VALUE = "length";

    @GET("ParseFile")
    Call<List<Vehicle>> loadVehicles();

    @GET("ParseFile")
    Call<List<Vehicle>> loadHistory(@Query("imei") String imei,
                              @Query("startDate") String startDate,
                              @Query("endDate") String endDate);

}