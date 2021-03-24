public class Topic extends Message {
    private String topic;
    private int time;

    public Topic( String author, String type, String topic, int time, String message) {
        super(author, type, message);
        this.topic=topic;
        this.time=time;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}