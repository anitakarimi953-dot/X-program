package common;

public class Message {

    private String type;
    private String content;

    public Message(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return type + "|" + content;
    }
}