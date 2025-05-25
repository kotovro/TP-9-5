package logic.general;

import java.util.ArrayList;

public class Replica {
    private Speaker speaker;
    private String text;
    private double timecode;

    public Replica() {
    }

    public Replica(String text, Speaker speaker, double timecode) {
        this.text = text;
        this.speaker = speaker;
        this.timecode = timecode;
    }

    public Speaker getSpeaker() {
        return speaker;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSpeaker(Speaker speaker) {
        this.speaker = speaker;
    }

    public String getText() {
        return text;
    }

    public double getTimecode() {
        return timecode;
    }

    public void setTimecode(double timecode) {
        this.timecode = timecode;
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
