package info.horriblesubs.sher.common;

import info.horriblesubs.sher.model.response.LatestResponse;
import info.horriblesubs.sher.model.response.ScheduleResponse;
import info.horriblesubs.sher.model.response.SearchResponse;
import info.horriblesubs.sher.model.response.ShowResponse;
import info.horriblesubs.sher.model.response.ShowsResponse;
import info.horriblesubs.sher.model.response.UpdatesResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface Api {
    String Link = "http://udit12.esy.es/api/hs/";

    @GET("latest/0")
    Call<LatestResponse> getLatest();

    @GET("schedule/0")
    Call<ScheduleResponse> getSchedule();

    @GET("search/{query}")
    Call<SearchResponse> getSearch(@Path(value = "query", encoded = true) String query);

    @GET("show/{link}")
    Call<ShowResponse> getShow(@Path(value = "link", encoded = true) String link);

    @GET("shows/{type}")
    Call<ShowsResponse> getShows(@Path(value = "type", encoded = true) String type);

    @GET("update/{version}")
    Call<UpdatesResponse> getUpdate(@Path(value = "version", encoded = true) int version);

    @GET
    Call<ResponseBody> downloadUpdate(@Url String url);
}