package logic.video_processing.vosk.analiseDTO;

import java.util.ArrayList;
import java.util.List;

public class RawTranscript {
    private static int untitledCount = 0;
    private int ID;

    private int speakerCount;
    List<String> phrases = new ArrayList<>();
    List<Integer> groupIDs = new ArrayList<>();

    public RawTranscript(int speakerCount, List<RawReplica> replicas) {
        this.speakerCount = speakerCount;
        for (RawReplica replica : replicas) {
            phrases.addLast(replica.text);
            groupIDs.addLast(replica.speaker.ID);
        }
        ID = untitledCount++;
    }

    public int getSpeakerCount() {
        return speakerCount;
    }

    public String getPhrase(int index) {
        return phrases.get(index);
    }

    public int getGroupID(int index) {
        return groupIDs.get(index);
    }

    public int getID() {
        return ID;
    }

    public int getPhraseCount() {
        return phrases.size();
    }
}
