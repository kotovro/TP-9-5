package logic.text_edit.action;

import logic.general.Replica;
import logic.general.Transcript;

public class RemoveReplica implements StoryPoint {
    private final Transcript transcript;
    private final int replicaPosition;
    private Replica removedReplica;

    public RemoveReplica(Transcript transcript, int replicaPosition) {
        this.transcript = transcript;
        this.replicaPosition = replicaPosition;
    }

    @Override
    public void apply() {
        if (removedReplica == null) removedReplica = transcript.getReplica(replicaPosition);
        transcript.removeReplica(replicaPosition);
    }

    @Override
    public void unapply() {
        transcript.addReplica(removedReplica, replicaPosition);
    }
}