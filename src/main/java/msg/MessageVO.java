package msg;

public class MessageVO {
    public String id;
    public String title;
    public String content;

    @Override
    public String toString() {
        return "MessageVO{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
