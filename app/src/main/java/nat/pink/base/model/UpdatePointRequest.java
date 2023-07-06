package nat.pink.base.model;

import com.google.gson.annotations.SerializedName;

public class UpdatePointRequest {
    @SerializedName("deviceId")
    private String deviceId;
    @SerializedName("updateType")
    private int updateType;
    @SerializedName("adjustPoint")
    private int adjustPoint;

    public UpdatePointRequest(String deviceId, int updateType, int adjustPoint) {
        this.deviceId = deviceId;
        this.updateType = updateType;
        this.adjustPoint = adjustPoint;
    }
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    public int getUpdateType() {
        return updateType;
    }

    public void setUpdateType(int updateType) {
        this.updateType = updateType;
    }

    public int getAdjustPoint() {
        return adjustPoint;
    }

    public void setAdjustPoint(int adjustPoint) {
        this.adjustPoint = adjustPoint;
    }
}
