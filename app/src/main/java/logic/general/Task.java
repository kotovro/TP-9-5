package logic.general;

public class Task {
    private int id;
    private String description;
    private int transcriptId;
    private int assigneeId = 1;

    public Task(int transcriptId, int assigneeId, String description) {
        this.transcriptId = transcriptId;
        this.description = description;
        this.assigneeId = assigneeId;
    }

    public Task(int transcriptId, String description) {
        this.transcriptId = transcriptId;
        this.description = description;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTranscriptId() {
        return transcriptId;
    }

    public void setTranscriptId(int transcriptId) {
        this.transcriptId = transcriptId;
    }

    public int getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(int assigneeId) {
        this.assigneeId = assigneeId;
    }
}
