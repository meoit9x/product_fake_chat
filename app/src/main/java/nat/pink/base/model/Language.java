package nat.pink.base.model;

import nat.pink.base.base.BaseModel;

public class Language extends BaseModel {
    private String language;
    private boolean isSelected;
    private String value;
    private Integer flags;

    public Language() {
    }

    public Language(String language, boolean isSelected, String value, Integer flags) {
        this.language = language;
        this.isSelected = isSelected;
        this.value = value;
        this.flags = flags;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getFlags() {
        return flags;
    }

    public void setFlags(Integer flags) {
        this.flags = flags;
    }
}
