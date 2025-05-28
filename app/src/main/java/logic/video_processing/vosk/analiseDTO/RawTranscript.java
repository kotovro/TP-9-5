package logic.video_processing.vosk.analiseDTO;

import logic.utils.TimeCode;
import logic.utils.TimeFormatter;

import java.util.ArrayList;
import java.util.List;

public class RawTranscript {
    private static int untitledCount = 0;
    private int ID;

    private int speakerCount;
    private List<String> phrases = new ArrayList<>();
    private List<Integer> groupIDs = new ArrayList<>();
    private List<TimeCode> timeCodes = new ArrayList<>();

    public RawTranscript(int speakerCount, List<RawReplica> replicas) {
        this.speakerCount = speakerCount;
        for (RawReplica replica : replicas) {
            phrases.addLast(replica.text);
            groupIDs.addLast(replica.speaker.ID);
            timeCodes.addLast(TimeFormatter.format(replica.getStartTime()));
        }
        ID = untitledCount++;
    }

    public int getSpeakerCount() {
        return speakerCount;
    }

    public String getPhrase(int index) {
        return phrases.get(index);
    }

    public TimeCode getTimeCode(int index) {
        return timeCodes.get(index);
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
