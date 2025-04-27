package logic.text_edit;

import javafx.util.Pair;
import logic.general.Replica;
import logic.general.Transcript;
import java.util.ArrayList;

public class FindResult {
    private ArrayList<Pair<Integer, Integer>> searchResults = new ArrayList<>();
    private int currentIndex = 0;

    public void formSearchResults(Transcript transcript, String searchText) {
        int replicaIndex = 0;
        for (Replica replica : transcript.getReplicas()) {
            for (Integer position : replica.findAllOccurrences(searchText)) {
                searchResults.add(new Pair<>(replicaIndex, position));
            }
            replicaIndex++;
        }
    }

    public boolean hasNext() {
        return currentIndex < searchResults.size() - 1;
    }

    public Pair<Integer, Integer> next() {
        currentIndex++;
        return searchResults.get(currentIndex);
    }

    public boolean hasPrevious() {
        return currentIndex > 0;
    }

    public Pair<Integer, Integer> previous() {
        currentIndex--;
        return searchResults.get(currentIndex);
    }
}
