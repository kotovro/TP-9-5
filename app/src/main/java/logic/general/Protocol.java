package logic.general;

public class Protocol {
    private int meetingId;
    private String text;

    public Protocol() {
    }

    public Protocol(int meetingId, String text) {
        this.meetingId = meetingId;
        this.text = text;
    }


    public int getMeetingId() {
        return meetingId;
    }

    public String getText() {
        return text;
    }
}

