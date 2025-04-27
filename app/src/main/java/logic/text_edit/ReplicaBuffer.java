package logic.text_edit;

import logic.general.Replica;

public class ReplicaBuffer {
    private static Replica bufferedReplica = null;

    public static boolean isEmpty() {
        return bufferedReplica == null;
    }

    public static void setReplica(Replica replica) {
        bufferedReplica = replica;
    }

    public static Replica getReplica() {
        return bufferedReplica;
    }

    public static void clear() {
        bufferedReplica = null;
    }
}
