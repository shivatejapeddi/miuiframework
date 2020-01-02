package android.inputmethodservice;

public class ClipboardContentModel {
    private String content;
    private int type;

    public ClipboardContentModel(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
