package logic.text_edit.action;

import javafx.util.Pair;
import logic.general.Replica;
import logic.general.Transcript;

import java.util.LinkedList;
import java.util.List;

public class FindAction {
    public String searchText;

    List<Pair<Replica, Integer>> searchResults = new LinkedList<>();
    List<Pair<Replica, Integer>> getSearchResults() {
        return searchResults;
    }

    public List<Pair<Replica, Integer>> search(Transcript transcript, String text)
    {
        searchText = text;
        for (Replica replica : transcript.getReplicas()) {
            Integer searchedTextIndex =  replica.getText().indexOf(searchText);
            searchResults.add(new Pair<>(replica, searchedTextIndex));
        }
        return searchResults;
    }

}
