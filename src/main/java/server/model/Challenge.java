package server.model;

public class Challenge {
    private final String to;
    private final String text;

    public Challenge(String to, String text) {
        this.to = to;
        this.text = text;
    }

    public String getTo() {
        return to;
    }

    public String getText() {
        return text;
    }
}
