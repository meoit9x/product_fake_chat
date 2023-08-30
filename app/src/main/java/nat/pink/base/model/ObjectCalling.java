package nat.pink.base.model;
import java.io.Serializable;

import nat.pink.base.dialog.DialogChangeTime;

public class ObjectCalling implements Serializable {

    private String name = "";
    private String message = "";
    private String pathImage;
    private String pathVideo;
    private String pathBackground = "";
    private DialogChangeTime.CHANGE_TYPE timer = DialogChangeTime.CHANGE_TYPE.TEN_SECONDS;
    private boolean isCalling;
    private boolean isSound = true;

    public boolean isCalling() {
        return isCalling;
    }

    public void setCalling(boolean calling) {
        isCalling = calling;
    }

    public ObjectCalling() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimer(DialogChangeTime.CHANGE_TYPE timer) {
        this.timer = timer;
    }

    public String getName() {
        return name;
    }

    public DialogChangeTime.CHANGE_TYPE getTimer() {
        return timer;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public String getPathVideo() {
        return pathVideo;
    }

    public void setPathVideo(String pathVideo) {
        this.pathVideo = pathVideo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public boolean isSound() {
        return isSound;
    }

    public void setSound(boolean sound) {
        isSound = sound;
    }
    public String getPathBackground() {
        return pathBackground;
    }

    public void setPathBackground(String pathBackground) {
        this.pathBackground = pathBackground;
    }
}
