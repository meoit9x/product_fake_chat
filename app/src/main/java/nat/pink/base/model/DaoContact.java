package nat.pink.base.model;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Comparator;

@Entity(tableName = "contact")
public class DaoContact implements Serializable, Comparable<DaoContact> {
    public DaoContact(int id, String name, int online, boolean verified, boolean is_friend, int color, String education, String work, String live, String avatar) {
        this.id = id;
        this.name = name;
        this.online = online;
        this.verified = verified;
        this.is_friend = is_friend;
        this.color = color;
        this.education = education;
        this.work = work;
        this.live = live;
        this.avatar = avatar;
    }

    public DaoContact(){

    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "online")
    private int online;

    @ColumnInfo(name = "verified")
    private boolean verified;

    @ColumnInfo(name = "is_friend")
    private boolean is_friend;

    @ColumnInfo(name = "color")
    private int color;

    @ColumnInfo(name = "education")
    private String education;

    @ColumnInfo(name = "work")
    private String work;

    @ColumnInfo(name = "live")
    private String live;

    @ColumnInfo(name = "avatar")
    private String avatar;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isIs_friend() {
        return is_friend;
    }

    public void setIs_friend(boolean is_friend) {
        this.is_friend = is_friend;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getLive() {
        return live;
    }

    public void setLive(String live) {
        this.live = live;
    }

    @Override
    public int compareTo(DaoContact daoContact) {
        return Comparators.NAME.compare(this, daoContact);
    }

    public static class Comparators {

        public static Comparator<DaoContact> NAME = (o1, o2) -> o1.name.compareTo(o2.name);
    }
}
