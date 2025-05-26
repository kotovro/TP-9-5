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
        ArrayList<Integer> indices = new ArrayList<>();

        if (searchText == null || searchText.isEmpty() || text == null) {
            return indices;
        }
        int pos = text.indexOf(searchText);
        while (pos != -1) {
            indices.add(pos);
            pos = text.indexOf(searchText, pos + 1);
        }
        return indices;
    }
}
