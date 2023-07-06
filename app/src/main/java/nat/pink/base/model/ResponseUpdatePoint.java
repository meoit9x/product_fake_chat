package nat.pink.base.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseUpdatePoint implements Serializable {
    @SerializedName("firstTime")
    @Expose
    private Boolean firstTime;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("points")
    @Expose
    private Integer points;
    @SerializedName("status")
    @Expose
    private Integer status;

    public Boolean getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Boolean firstTime) {
        this.firstTime = firstTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
