package logic.general;

import java.util.ArrayList;
import java.util.List;

public class Replica {
    private Participant participant;
    private StringBuilder text;

    public Replica(String text) {
        this.text = new StringBuilder(text);
    }

    public int getSize() {
        return 0;
    }

    public Participant getSpeaker() {
        return participant;
    }


    public void setSpeaker(Participant participant) {
        this.participant = participant;
    }

    public StringBuilder getText() {
        return text;
    }

    public ArrayList<Integer> findAllOccurrences(String searchText) {
        if (searchText.isEmpty())
            return new ArrayList<>();

        ArrayList<Integer> indices = new ArrayList<>();
        int searchLength = searchText.length();
        int textLength = text.length();
        int index = 0;

        while (index <= textLength - searchLength) {
            boolean found = true;

            for (int i = 0; i < searchLength; i++) {
                if (text.charAt(index + i) != searchText.charAt(i)) {
                    found = false;
                    break;
                }
            }

            if (found) {
                indices.add(index);
                index += searchLength;
            } else {
                index++;
            }
        }
        return indices;
    }
}
