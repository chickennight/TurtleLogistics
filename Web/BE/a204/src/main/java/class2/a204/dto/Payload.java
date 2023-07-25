package class2.a204.dto;

public class Payload {
    private String topic;

    private String message;

    public Payload(String topic, String message) {
        this.topic = topic;
        this.message = message;
    }

    public Payload() {
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
