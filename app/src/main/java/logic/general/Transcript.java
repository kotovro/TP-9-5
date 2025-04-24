package logic.general;

import javafx.util.Pair;
import logic.text_edit.ReplicaBuffer;

import java.util.Date;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

public class Transcript {
    protected List<Replica> replicas = new ArrayList<>();
    protected int currentIndex = 0;
    private String name;
    private Date date;

    public Transcript(String name, Date date) {
        this.name = name;
        this.date = date;
    }

    public void addReplica(Replica replica, int index) {
        replicas.add(index, replica);
    }

    public void addReplica(Replica replica) {
        replicas.add(currentIndex, replica);
    }

    public void removeReplica(int index) {
        replicas.remove(index);
    }

    public void removeReplica(Replica replica) {
        replicas.remove(replica);
    }

    public void cutReplica(int index) {
        ReplicaBuffer.setReplica(replicas.get(index));
        removeReplica(index);
    }

    public Iterable<Replica> getReplicas() {
        return replicas;
    }

    public Replica getReplica(int index) {
        return replicas.get(index);
    }

    public Replica getCurrentReplica() {
        return replicas.get(currentIndex);
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    List<Pair<Replica, Integer>> searchResults = new LinkedList<>();
    List<Pair<Replica, Integer>> getSearchResults() {
        return searchResults;
    }

    public List<Pair<Replica, Integer>> findText(Transcript transcript, String searchText)
    {
        for (Replica replica : transcript.getReplicas()) {
            Integer searchedTextIndex =  replica.getText().indexOf(searchText);
            searchResults.add(new Pair<>(replica, searchedTextIndex));
        }
        return searchResults;
    }

}
