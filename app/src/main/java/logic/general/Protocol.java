package logic.general;

public class Protocol {
    private String text;
    private int transcriptId;

    public Protocol() {
    }

    public Protocol(int transcriptId, String text) {
        this.transcriptId = transcriptId;
        this.text = text;
    }

    public Protocol(String text) {
        this.text = text;
    }

    public int getTranscriptId() {
        return transcriptId;
    }

    public String getText() {
        return text;
    }
}

