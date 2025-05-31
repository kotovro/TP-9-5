package ui.custom_elements;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import logic.general.*;
import logic.persistence.DBManager;
import logic.utils.EntitiesExporter;
import ui.BaseController;
import ui.custom_elements.combo_boxes.SearchableComboBox;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProtocolDisplayer extends BaseDisplayer {
    private final MeetingMaterials meetingMaterials;

    public ProtocolDisplayer(MeetingMaterials meetingMaterials, List<Speaker> speakers, BaseController baseController) {
        super(meetingMaterials.getTranscript().getName() + ".pr", speakers, baseController);
        if (meetingMaterials.getProtocol().isEmpty()) {
            throw new RuntimeException("Materials don't contain any protocol");
        }
        this.meetingMaterials = meetingMaterials;
        initTextArea();
    }

    @Override
    protected void initTextArea() {
        textAreaContainer.getChildren().add(formReplicaView(
                new Replica(meetingMaterials.getProtocol().get().getText(), speakers.get(1), 0)));
        for (Task task : meetingMaterials.getTasks()) {
            int speakerIndex = task.getAssigneeId() == 1 ? 2 : task.getAssigneeId() - 1;
            textAreaContainer.getChildren().add(formReplicaView(new Replica(task.getDescription(),
                    speakers.get(speakerIndex), 0)));
        }
        if (textAreaContainer.getChildren().isEmpty()) {
            lockSave();
        }
    }

    @Override
    protected void unlockSave() {
        save.setDisable(false);
        export.setDisable(false);
    }

    @Override
    protected void lockSave() {
        save.setDisable(true);
        export.setDisable(true);
    }

    @Override
    protected void initButtonsActions(Button save, Button saveAs, Button export) {
        save.setOnAction(e -> {
            save();
        });
        saveAs.setDisable(true);
        export.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setInitialFileName("protocol.txt");
            File file = fileChooser.showSaveDialog(export.getScene().getWindow());

            List<Node> nodes = textAreaContainer.getChildren();
            Protocol newProtocol = new Protocol(meetingMaterials.getTranscript().getId(), ((BasePane)nodes.getFirst()).textarea.getText());
            EntitiesExporter.exportProtocolToTextFile(meetingMaterials.getTranscript(), newProtocol, file);
        });
    }

    protected BasePane formReplicaView(Replica replica) {
        ComboBox<Speaker> comboBox = new SearchableComboBox(speakers, replica.getSpeaker(), baseController);
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
        DBManager.getProtocolDao().updateProtocol(newProtocol);
        DBManager.getTaskDao().deleteTasksByTranscript(meetingMaterials.getTranscript().getId());
        for (Task task : tasks) {
            DBManager.getTaskDao().addTask(task);
        }
    }
}
