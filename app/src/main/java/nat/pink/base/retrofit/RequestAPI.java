package nat.pink.base.retrofit;

import androidx.annotation.RawRes;
import androidx.room.RawQuery;

import nat.pink.base.model.DeviceRequest;
import nat.pink.base.model.FeedbackRequest;
import nat.pink.base.model.ResponseDevice;
import nat.pink.base.model.ResponseFeedback;
import nat.pink.base.model.ResponseLeaderBoard;
import nat.pink.base.model.ResponseUpdatePoint;
import nat.pink.base.model.UpdatePointRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RequestAPI {
    @POST("api/feedback")
    Call<ResponseFeedback> feedback(@Body FeedbackRequest feedbackRequest);

    @POST("api/point")
    Call<ResponseUpdatePoint> updatePoint(@Body UpdatePointRequest updatePointRequest);

    @POST("api/point/device")
    Call<ResponseDevice> getPoint(@Body DeviceRequest deviceRequest);

    @POST("api/point/leaderboard")
    Call<ResponseLeaderBoard> getLeaderBoard();
}
