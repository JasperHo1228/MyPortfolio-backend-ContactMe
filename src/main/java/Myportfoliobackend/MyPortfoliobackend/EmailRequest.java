package Myportfoliobackend.MyPortfoliobackend;

public class EmailRequest {
    private String recipient;
    private String subject;
    private String message;

    // Getters and setters

    public String getRecipient() {
        return recipient;
    }

    public void setSender(String sender) {
        this.recipient = sender;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
