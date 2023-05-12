package nat.pink.base.model;

import android.graphics.Bitmap;

public class FakeUser {
    private String displayName;
    private Bitmap avatar;
    private boolean isOnline;
    //etc....


    public FakeUser(String displayName, Bitmap avatar, boolean isOnline) {
        this.displayName = displayName;
        this.avatar = avatar;
        this.isOnline = isOnline;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}
