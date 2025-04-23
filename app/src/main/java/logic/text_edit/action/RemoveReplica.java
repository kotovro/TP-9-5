package logic.text_edit.action;

import logic.general.Replica;
import logic.general.Transcript;

public class RemoveReplica implements Executable {
    public Transcript transcript;
    public int replicaPosition;
    private Replica removedReplica;
    private boolean removed = false;

    public RemoveReplica(Transcript transcript, int replicaPosition) {
        this.transcript = transcript;
        this.replicaPosition = replicaPosition;
    }

    @Override
    public void apply() {
        if (!removed) {
            if (replicaPosition >= 0 && replicaPosition < transcript.getReplicas().size()) {
                removedReplica = transcript.getReplicas().remove(replicaPosition);
                removed = true;
            }
        }
    }

    @Override
    public void unapply() {
        if (removed && removedReplica != null) {
            transcript.getReplicas().add(replicaPosition, removedReplica);
            removed = false;
        }
    }
}