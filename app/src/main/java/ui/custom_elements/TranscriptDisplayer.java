package ui.custom_elements;

import javafx.scene.Node;
import javafx.scene.control.Button;
import logic.general.Replica;
import logic.general.Speaker;
import logic.general.Transcript;
import logic.persistence.DBManager;
import ui.BaseController;

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
    }

    @Override
    protected void initButtonsActions(Button save, Button saveAs) {
        save.setOnAction(e -> {
            save();
        });

        saveAs.setOnAction(e -> {
            baseController.loadSaveAsDialog(this::saveAs);
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
