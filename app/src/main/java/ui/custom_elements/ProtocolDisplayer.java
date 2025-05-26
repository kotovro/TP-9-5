package ui.custom_elements;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import logic.general.*;
import logic.persistence.DBManager;
import ui.custom_elements.combo_boxes.SearchableComboBox;

import java.util.ArrayList;
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
                new Replica(meetingMaterials.getProtocol().get().getText(), speakers.get(1), 0)));
        for (Task task : meetingMaterials.getTasks()) {
            textAreaContainer.getChildren().add(formReplicaView(
                    new Replica(task.getDescription(), speakers.get(2), 0)));
        }
    }

    @Override
    protected void initButtonsActions(Button save, Button saveAs) {
        save.setOnAction(e -> {
            save();
        });

        saveAs.setDisable(true);
    }

    protected BasePane formReplicaView(Replica replica) {
        ComboBox<Speaker> comboBox = new SearchableComboBox(speakers, replica.getSpeaker());
        TextArea textArea = initTextArea(replica.getText());
        BasePane basepane = new BasePane(comboBox, textArea, this);
        VBox.setMargin(basepane, new Insets(10, 50, 0, 50));
        return basepane;
    }

    private void save() {
        List<Node> nodes = textAreaContainer.getChildren();
        Protocol newProtocol = new Protocol(meetingMaterials.getTranscript().getId(), ((BasePane)nodes.getFirst()).textarea.getText());
        List<Task> tasks = new ArrayList<>();
        for (int i = 1; i < nodes.size(); i++) {
            BasePane pane = (BasePane) nodes.get(i);
            tasks.addLast(new Task(meetingMaterials.getTranscript().getId(),
                    pane.combobox.getSelectionModel().getSelectedItem().getId(), pane.textarea.getText()));
        }
        DBManager.getProtocolDao().addProtocol(newProtocol);
        for (Task task : tasks) {
            DBManager.getTaskDao().addTask(task);
        }
    }
}
