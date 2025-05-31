package ui.custom_elements;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import logic.general.Replica;
import logic.general.Speaker;
import logic.general.Transcript;
import logic.persistence.DBManager;
import logic.utils.EntitiesExporter;
import ui.BaseController;

import java.io.File;
import java.util.List;

public class TranscriptDisplayer extends BaseDisplayer {
    private final Transcript transcript;

    public TranscriptDisplayer(Transcript transcript, List<Speaker> speakers, BaseController baseController) {
        super(transcript.getName() + ".tr", speakers, baseController);
        this.transcript = transcript;
        initTextArea();
    }

    @Override
    protected void initTextArea() {
        for (Replica replica : transcript.getReplicas()) {
            textAreaContainer.getChildren().add(formReplicaView(replica));
        }
        if (textAreaContainer.getChildren().isEmpty()) lockSave();
    }

    @Override
    protected void unlockSave() {
        save.setDisable(false);
        saveAs.setDisable(false);
        export.setDisable(false);
    }

    @Override
    protected void lockSave() {
        save.setDisable(true);
        saveAs.setDisable(true);
        export.setDisable(true);
    }

    @Override
    protected void initButtonsActions(Button save, Button saveAs, Button export) {
        save.setOnAction(e -> {
            save();
        });

        saveAs.setOnAction(e -> {
            baseController.loadSaveAsDialog(this::saveAs);
        });

        export.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setInitialFileName("transcript.txt");
            File file = fileChooser.showSaveDialog(export.getScene().getWindow());

            Transcript newTranscript = new Transcript(transcript.getName(), transcript.getDate());
            for (Node node : textAreaContainer.getChildren()) {
                BasePane pane = (BasePane) node;
                newTranscript.addReplica(pane.getReplica());
            }
            EntitiesExporter.exportTranscriptToTextFile(newTranscript, file.getAbsolutePath());
        });
    }

    private void save() {
        Transcript newTranscript = new Transcript(transcript.getName(), transcript.getDate());
        newTranscript.setId(transcript.getId());
        for (Node node : textAreaContainer.getChildren()) {
            BasePane pane = (BasePane) node;
            newTranscript.addReplica(pane.getReplica());
        }
        DBManager.getTranscriptDao().updateTranscript(newTranscript);
    }

    private void saveAs(String name) {
        Transcript newTranscript = new Transcript(name, transcript.getDate());
        for (Node node : textAreaContainer.getChildren()) {
            BasePane pane = (BasePane) node;
            newTranscript.addReplica(pane.getReplica());
        }
        DBManager.getTranscriptDao().addTranscript(newTranscript);
    }
}
