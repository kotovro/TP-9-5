package logic.text_edit.action;

import logic.general.Replica;
import logic.general.Transcript;

public class AddReplica implements Executable {
    public Transcript transcript;
    public Replica replica;
    public int replicaPosition;
    private boolean inserted = false;

    public AddReplica(Transcript transcript, Replica replica, int replicaPosition) {
        this.transcript = transcript;
        this.replica = replica;
        this.replicaPosition = replicaPosition;
    }

    @Override
    public void apply() {
        if (!inserted) {
            if (replicaPosition >= 0 && replicaPosition <= transcript.getReplicas().size()) {
                transcript.getReplicas().add(replicaPosition, replica);
                inserted = true;
            }
        }
    }

    @Override
    public void unapply() {
        if (inserted) {
            transcript.getReplicas().remove(replicaPosition);
            inserted = false;
        }
    }
}