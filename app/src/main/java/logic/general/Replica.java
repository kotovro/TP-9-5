package logic.general;

public class Replica {
    private Participant participant;
    private String text;

    public int getSize() {
        return 0;
    }

    public Participant getSpeaker() {
        return participant;
    }

    public void setSpeaker(Participant participant) {
        this.participant = participant;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
