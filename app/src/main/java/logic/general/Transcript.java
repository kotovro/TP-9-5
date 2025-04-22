package logic.general;

import javafx.util.Pair;
import logic.text_edit.ReplicaBuffer;
import logic.text_edit.TextBuffer;

import java.util.ArrayList;
//<<<<<<< Updated upstream
//=======
import java.util.LinkedList;
//>>>>>>> Stashed changes
import java.util.List;

public class Transcript {
    List<Replica> replicas = new ArrayList<>();
    public Iterable<Replica> getReplicas() {
        return replicas;
    }

    public void removeReplica(int index) {
        replicas.remove(index);
    }

    public void cutReplica(int index) {
        ReplicaBuffer.setReplica(replicas.get(index));
        removeReplica(index);
    }

    public void addReplica(Replica replica, int index) {
        replicas.add(index, replica);
    }

    public Replica getReplica(int index) {
        return replicas.get(index);
    }

    public Iterable<Pair<Replica, Integer>> findText(Transcript transcript, String searchText) {
        List<Pair<Replica, Integer>> searchResults = new LinkedList<>();
        for (Replica replica : transcript.getReplicas()) {
            Integer searchedTextIndex =  replica.getText().indexOf(searchText);
            searchResults.add(new Pair<>(replica, searchedTextIndex));
        }
        return searchResults;
    }

}
