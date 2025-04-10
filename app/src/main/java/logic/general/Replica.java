package logic.general;

public class Replica {
    private Speaker speaker;
    private String text;

    public int getSize() {
        return 0;
    }

    public Speaker getSpeaker() {
        return speaker;
    }

    public void setSpeaker(Speaker speaker) {
        this.speaker = speaker;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
