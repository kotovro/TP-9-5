package ui.custom_elements;

import logic.general.Replica;
import logic.general.Speaker;
import logic.general.Transcript;
import java.util.List;

public class TranscriptDisplayer extends BaseDisplayer {
    private final Transcript transcript;

    public TranscriptDisplayer(Transcript transcript, List<Speaker> speakers) {
        super(transcript.getName(), speakers);
        this.transcript = transcript;
        initTextArea();
    }

    @Override
    public void saveToDB() {

    }

    @Override
    protected void initTextArea() {
        for (Replica replica : transcript.getReplicas()) {
            textAreaContainer.getChildren().add(formReplicaView(replica));
        }
    }
}
