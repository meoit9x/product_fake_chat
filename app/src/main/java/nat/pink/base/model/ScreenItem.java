package nat.pink.base.model;

public class ScreenItem {
    String Title, Description;
    int ScreenImg,BgImg;

    public ScreenItem(String title, String description, int screenImg,int bgImg){
        Title = title;
        Description =description;
        ScreenImg = screenImg;
        BgImg = bgImg;
    }

    public int getBgImg() {
        return BgImg;
    }

    public void setBgImg(int bgImg) {
        BgImg = bgImg;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getScreenImg() {
        return ScreenImg;
    }

    public void setScreenImg(int screenImg) {
        ScreenImg = screenImg;
    }
}
