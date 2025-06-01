package logic.integration_test.video_processing_test.vosk_test;

import logic.video_processing.vosk.analiseDTO.RawReplica;
import logic.video_processing.vosk.analiseDTO.RawSpeaker;
import logic.video_processing.vosk.analiseDTO.RawTranscript;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RawTranscriptTest {

    @Test
    void testTranscriptCreation() {
        List<RawReplica> replicas = new ArrayList<>();
        RawSpeaker speaker1 = new RawSpeaker(0, new double[]{0.1, 0.2, 0.3});
        RawSpeaker speaker2 = new RawSpeaker(1, new double[]{0.4, 0.5, 0.6});

        replicas.add(new RawReplica("Привет, как дела?", speaker1, new double[]{0.1, 0.2, 0.3}, 100));
        replicas.add(new RawReplica("Все хорошо, спасибо!", speaker2, new double[]{0.4, 0.5, 0.6}, 100));

        RawTranscript transcript = new RawTranscript(2, replicas);

        assertEquals(2, transcript.getSpeakerCount(), "Should have correct speaker count");
        assertTrue(transcript.getID() >= 0, "Should have non-negative ID");
    }

    @Test
    void testPhraseRetrieval() {
        List<RawReplica> replicas = new ArrayList<>();
        RawSpeaker speaker = new RawSpeaker(0, new double[]{0.1, 0.2, 0.3});

        String phrase1 = "Первая фраза";
        String phrase2 = "Вторая фраза";

        replicas.add(new RawReplica(phrase1, speaker, new double[]{0.1, 0.2, 0.3}, 100));
        replicas.add(new RawReplica(phrase2, speaker, new double[]{0.1, 0.2, 0.3}, 100));

        RawTranscript transcript = new RawTranscript(1, replicas);

        assertEquals(phrase1, transcript.getPhrase(0), "Should return correct first phrase");
        assertEquals(phrase2, transcript.getPhrase(1), "Should return correct second phrase");
    }

    @Test
    void testGroupIDRetrieval() {
        List<RawReplica> replicas = new ArrayList<>();
        RawSpeaker speaker1 = new RawSpeaker(0, new double[]{0.1, 0.2, 0.3});
        RawSpeaker speaker2 = new RawSpeaker(1, new double[]{0.4, 0.5, 0.6});

        replicas.add(new RawReplica("Фраза 1", speaker1, new double[]{0.1, 0.2, 0.3}, 100));
        replicas.add(new RawReplica("Фраза 2", speaker2, new double[]{0.4, 0.5, 0.6}, 100));

        RawTranscript transcript = new RawTranscript(2, replicas);

        assertEquals(0, transcript.getGroupID(0), "Should return correct first speaker ID");
        assertEquals(1, transcript.getGroupID(1), "Should return correct second speaker ID");
    }

    @Test
    void testEmptyTranscript() {
        List<RawReplica> replicas = new ArrayList<>();
        RawTranscript transcript = new RawTranscript(0, replicas);

        assertEquals(0, transcript.getSpeakerCount(), "Empty transcript should have zero speakers");
        assertTrue(transcript.getID() >= 0, "Should have non-negative ID even when empty");
    }

    @Test
    void testUniqueIDs() {
        List<RawReplica> replicas = new ArrayList<>();
        RawSpeaker speaker = new RawSpeaker(0, new double[]{0.1, 0.2, 0.3});
        replicas.add(new RawReplica("Тест", speaker, new double[]{0.1, 0.2, 0.3}, 100));

        RawTranscript transcript1 = new RawTranscript(1, replicas);
        RawTranscript transcript2 = new RawTranscript(1, replicas);

        assertNotEquals(transcript1.getID(), transcript2.getID(),
                "Different transcripts should have different IDs");
    }
}