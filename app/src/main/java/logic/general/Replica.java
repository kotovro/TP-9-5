package logic.general;

import java.util.ArrayList;
import java.util.Objects;

public class Replica {
    private static int maxId = 0;
    private int id;
    private Speaker speaker;
    private StringBuilder text;

    public Replica(String text, Speaker speaker) {
        this.text = new StringBuilder(text);
        id = maxId++;
    }

    public int getSize() {
        return 0;
    }

    public Speaker getSpeaker() {
        return speaker;
    }


    public void setSpeaker(Speaker speaker) {
        this.speaker = speaker;
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Replica replica)) return false;
        return id == replica.id;
    }
}
