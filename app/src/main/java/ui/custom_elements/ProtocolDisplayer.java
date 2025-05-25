package ui.custom_elements;

import javafx.scene.Node;
import logic.general.*;
import logic.persistence.DBManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ProtocolDisplayer extends BaseDisplayer {
    private final MeetingMaterials meetingMaterials;

    public ProtocolDisplayer(MeetingMaterials meetingMaterials, List<Speaker> speakers) {
        super(meetingMaterials.getTranscript().getName() + ".pr", speakers);
        if (meetingMaterials.getProtocol().isEmpty()) {
            throw new RuntimeException("Materials don't contain any protocol");
        }
        this.meetingMaterials = meetingMaterials;
        initTextArea();
    }

    public void saveToDB() {
        Protocol protocol = meetingMaterials.getProtocol().get();

        List<Replica> replicas = new LinkedList<>();
        for (Node node : textAreaContainer.getChildren()) {
            BasePane pane = (BasePane) node;
            replicas.add(pane.getReplica());
        }
        Protocol newProtocol = new Protocol(protocol.getTranscriptId(), replicas.getFirst().getText());
        DBManager.getProtocolDao().addProtocol(newProtocol);

        replicas.removeFirst();
        for (Replica replica : replicas) {
            DBManager.getTaskDao().addTask(new Task(meetingMaterials.getTranscript().getId(), replica.getSpeaker().getId(), replica.getText()));
        }
    }

    @Override
    protected void initTextArea() {
        textAreaContainer.getChildren().add(formReplicaView(
                new Replica(meetingMaterials.getProtocol().get().getText(), speakers.get(1))));
        for (Task task : meetingMaterials.getTasks()) {
            textAreaContainer.getChildren().add(formReplicaView(
                    new Replica(task.getDescription(), speakers.get(2))));
        }
    }
}
