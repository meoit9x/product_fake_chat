package nat.pink.base.model;

import java.io.Serializable;

public class ObjectLanguage implements Serializable {

    private String language;
    private Boolean isSelected = false;
    private String value;
    private int flags;

    public ObjectLanguage(String language, String value, int flags) {
        this.language = language;
        this.value = value;
        this.flags = flags;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public ObjectLanguage() {
    }

    public String getLanguage() {
        return language;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public String getValue() {
        return value;
    }

    public int getFlags() {
        return flags;
    }
}
