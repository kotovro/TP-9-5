package logic.general;

import javafx.util.Pair;
import logic.text_edit.Buffer;

import java.util.ArrayList;
//<<<<<<< Updated upstream
//=======
import java.util.LinkedList;
//>>>>>>> Stashed changes
import java.util.List;

public class Transcript {
    List<Replica> transcript = new ArrayList<Replica>();
    public List<Replica> getReplicas() {
        return transcript;
    }

    public void removeReplica(int index) {

    }

    public void cutReplica(int index, Buffer buffer) {

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
