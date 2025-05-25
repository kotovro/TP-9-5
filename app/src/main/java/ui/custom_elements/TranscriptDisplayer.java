package ui.custom_elements;

import javafx.scene.Node;
import logic.general.Replica;
import logic.general.Speaker;
import logic.general.Transcript;
import logic.persistence.DBManager;

import java.util.Date;
import java.util.List;

public class TranscriptDisplayer extends BaseDisplayer {
    private final Transcript transcript;

    public TranscriptDisplayer(Transcript transcript, List<Speaker> speakers) {
        super(transcript.getName() + ".tr", speakers);
        this.transcript = transcript;
        initTextArea();
    }

    public void saveToDB() {
        Transcript newTranscript = new Transcript(transcript.getName(), transcript.getDate());
        for (Node node : textAreaContainer.getChildren()) {
            BasePane pane = (BasePane) node;
            newTranscript.addReplica(pane.getReplica());
        }
        DBManager.getTranscriptDao().addTranscript(newTranscript);
    }

    @Override
    protected void initTextArea() {
        for (Replica replica : transcript.getReplicas()) {
            textAreaContainer.getChildren().add(formReplicaView(replica));
        }
    }
}
