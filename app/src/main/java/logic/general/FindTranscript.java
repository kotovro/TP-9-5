package logic.general;

import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;

public class FindTranscript extends Transcript {

    public Iterable<Pair<Replica, Integer>> findText(String searchText) {
        List<Pair<Replica, Integer>> searchResults = new LinkedList<>();
        for (Replica replica : replicas) {
            for (Integer index : replica.findAllOccurrences(searchText)) {
                searchResults.add(new Pair<>(replica, index));
            }
        }
        return searchResults;
    }
}
