
public class Message {

    private String message;
    private String type;
    private String author;

    public Message( String author, String type, String message) {
        this.author = author;
        this.message = message;
        this.type = type;
    }

    public String getAuthor() { return author; }

    public void setAuthor(String author) { this.author = author; }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}