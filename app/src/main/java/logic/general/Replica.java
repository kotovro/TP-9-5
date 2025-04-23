package logic.general;

public class Replica {
    private Participant participant;
    private StringBuilder text;

    public Replica(String text) {
        this.text = new StringBuilder(text);
    }

    public int getSize() {
        return 0;
    }

    public Participant getSpeaker() {
        return participant;
    }


    public void setSpeaker(Participant participant) {
        this.participant = participant;
    }

    public StringBuilder getText() {
        return text;
    }
}
