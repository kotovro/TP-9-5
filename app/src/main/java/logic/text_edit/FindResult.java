package logic.general;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Date;

public class FindTranscript extends Transcript {
    private ArrayList<Pair<Integer, Integer>> searchResults = new ArrayList<>();
    private int currentIndex = 0;

    public FindTranscript(String name, Date date) {
        super(name, date);
    }

    public Iterable<Pair<Replica, Integer>> formSearchResults(String searchText) {
        for (Replica replica : replicas) {
            for (Integer index : replica.findAllOccurrences(searchText)) {
                searchResults.add(new Pair<>(replica, index));
            }
        }
        return searchResults;
    }

    public boolean hasNext() {
        return currentIndex < searchResults.size();
    }

    p
}
