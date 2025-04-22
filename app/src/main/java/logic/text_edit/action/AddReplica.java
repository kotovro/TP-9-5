package logic.text_edit.action;

import logic.general.Replica;
import logic.general.Transcript;

public class AddReplica implements StoryPoint {
    private final Transcript transcript;
    private final Replica replica;
    private final int replicaPosition;

    public AddReplica(Transcript transcript, Replica replica, int replicaPosition) {
        this.transcript = transcript;
        this.replica = replica;
        this.replicaPosition = replicaPosition;
    }

    @Override
    public void apply() {
        transcript.addReplica(replica, replicaPosition);
    }

    @Override
    public void unapply() {
        transcript.removeReplica(replicaPosition);
    }
}