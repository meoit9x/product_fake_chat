package nat.pink.base.model;

import java.io.Serializable;
import java.util.List;

public class ObjectFaq implements Serializable {
    private String title;
    private List<String> content;

    public ObjectFaq(String title, List<String> content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }
}
