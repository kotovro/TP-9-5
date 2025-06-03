package logic.general;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MeetingMaterials materials)) return false;
        return Objects.equals(transcript.getId(), materials.transcript.getId());
    }
}
