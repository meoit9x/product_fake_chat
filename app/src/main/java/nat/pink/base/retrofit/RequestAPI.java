package nat.pink.base.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RequestAPI {
    @POST("api/feedback")
    Call<String> feedback(@Query("package") String pack, @Query("version") String version, @Query("content") String content);
}
