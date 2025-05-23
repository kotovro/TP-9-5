package ui.custom_elements;

import logic.general.*;

import java.util.List;

public class ProtocolDisplayer extends BaseDisplayer {
    private final Protocol protocol;
    private final List<Task> tasks;

    public ProtocolDisplayer(Protocol protocol, Transcript transcript, List<Speaker> speakers, List<Task> tasks) {
        super(transcript.getName(), speakers);
        this.protocol = protocol;
        this.tasks = tasks;
        initTextArea();
    }

    @Override
    public void saveToDB() {

    }

    @Override
    protected void initTextArea() {
        textAreaContainer.getChildren().add(formReplicaView(new Replica(protocol.getText(), speakers.getFirst())));
        for (Task task : tasks) {
            textAreaContainer.getChildren().add(formReplicaView(new Replica(task.getDescription(), speakers.getFirst())));
        }
    }
}
