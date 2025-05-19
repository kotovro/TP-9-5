package logic.general;

import javafx.concurrent.Task;
import javafx.util.Pair;
import logic.text_edit.ReplicaBuffer;

import java.util.Date;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

public class Transcript {
    private int id = -1;
    protected List<Replica> replicas = new ArrayList<>();
    private List<Tag> tags = new ArrayList<>();
    private String name;
    private Date date;


    public void setReplicas(List<Replica> replicas) {
        this.replicas = replicas;
    }
    public void setTags(List<Tag> tags) {this.tags = tags;}

    public Transcript(String name, Date date) {
        this.name = name;
        this.date = date;

    }

    public void addReplica(Replica replica, int index) {
        replicas.add(index, replica);
    }

    public void addReplica(Replica replica) {
        replicas.addLast(replica);
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

    public Iterable<Tag> getTags() {
        return tags;
    }

    public Replica getReplica(int index) {
        return replicas.get(index);
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
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
