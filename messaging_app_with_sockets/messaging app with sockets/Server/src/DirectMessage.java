public class DirectMessage extends Message{
    private String recipient;
    public DirectMessage (String author, String type, String recipient,String message){
        super(author,type,message);
        this.recipient=recipient;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
}
