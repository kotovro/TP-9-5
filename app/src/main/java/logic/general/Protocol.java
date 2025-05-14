package logic.general;

public class Protocol {
    private String text;
    private String transcriptName;

    public Protocol() {
    }

    public Protocol(String transcriptName, String text) {
        this.transcriptName = transcriptName;
        this.text = text;
    }

    public Protocol(String text) {
        this.text = text;
    }

    public String getTranscriptName() {
        return transcriptName;
    }

    public String getText() {
        return text;
    }
}

