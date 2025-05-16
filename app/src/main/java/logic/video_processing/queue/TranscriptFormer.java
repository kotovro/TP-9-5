package logic.video_processing.queue;

import logic.general.Replica;
import logic.general.Speaker;
import logic.general.Transcript;
import logic.persistence.DBManager;
import logic.video_processing.ProcessStatus;
import logic.video_processing.vosk.analiseDTO.RawReplica;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class TranscriptFormer {
    private static int untitledCount = 0;
    public static Transcript formTranscript(List<RawReplica> rawReplicas) throws SQLException {
        Transcript transcript = new Transcript("untitled" + untitledCount, new Date());
        for (RawReplica replica : rawReplicas) {
            Speaker speaker = DBManager.getSpeakerDao().getSpeakerById(1);
            transcript.addReplica(new Replica(replica.text, speaker));

        }
        untitledCount++;
        return transcript;
    }
}
