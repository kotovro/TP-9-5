package logic.general;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MeetingMaterials {
    private Transcript transcript;
    private Optional<Protocol> protocol;
    private List<Task> tasks = new ArrayList<>();

    public MeetingMaterials(Transcript transcript, Optional<Protocol> protocol, List<Task> tasks) {
        this.transcript = transcript;
        this.protocol = protocol;
        this.tasks = tasks;
    }

    public Transcript getTranscript() {
        return transcript;
    }

    public Optional<Protocol> getProtocol() {
        return protocol;
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
