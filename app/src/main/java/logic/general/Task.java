package logic.general;

public class Task {
    private int id;
    private String name;
    private String description;
    private int transcriptId;

    public Task(int transcriptId, String name, String description) {
        this.transcriptId = transcriptId;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }


    public int getTranscriptId() {
        return transcriptId;
    }

}
