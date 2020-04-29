package network;

import java.util.List;

import model.EventsDataModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GetDataService {
    @GET("api/events")
    Call<List<EventsDataModel>> getAllData();
    @GET
    Call<List<EventsDataModel>> getUpdatedDataList(@Url String url);
}
