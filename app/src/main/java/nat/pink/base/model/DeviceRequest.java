package nat.pink.base.model;

import com.google.gson.annotations.SerializedName;

public class DeviceRequest {
    @SerializedName("deviceId")
    private String deviceId;

    public DeviceRequest(String deviceId) {
        this.deviceId = deviceId;
    }
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
