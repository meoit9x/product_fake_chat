package nat.pink.base.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataUserCoin {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("device_id")
    @Expose
    private String deviceId;
    @SerializedName("point")
    @Expose
    private Integer point;
    @SerializedName("last_update")
    @Expose
    private String lastUpdate;

    public DataUserCoin(Integer id, String deviceId, Integer point, String lastUpdate) {
        this.id = id;
        this.deviceId = deviceId;
        this.point = point;
        this.lastUpdate = lastUpdate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
