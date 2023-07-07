package nat.pink.base.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedbackRequest {
    @SerializedName("package")
    @Expose
    private String _package;
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("content")
    @Expose
    private String content;

    public FeedbackRequest(String _package, String version, String content) {
        this._package = _package;
        this.version = version;
        this.content = content;
    }

    public String getPackage() {
        return _package;
    }

    public void setPackage(String _package) {
        this._package = _package;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
