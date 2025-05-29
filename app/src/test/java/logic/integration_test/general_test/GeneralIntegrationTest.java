package logic.integration_test.general_test;

import javafx.scene.image.Image;
import logic.general.*;
import logic.text_edit.ReplicaBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GeneralIntegrationTest {

    @Mock
    private Image image;

    private Transcript transcript;
    private Speaker speaker1;
    private Speaker speaker2;
    private Replica replica1;
    private Replica replica2;
    private Tag tag;
    private Task task;
    private Protocol protocol;

    @BeforeEach
    void setUp() {
        transcript = new Transcript("Тестовая транскрипция", new Date());
        transcript.setId(1);

        speaker1 = new Speaker("Алиса", image, 1);
        speaker2 = new Speaker("Борис", image, 2);

        replica1 = new Replica("Здравствуйте, это говорит Алиса.", speaker1, 1);
        replica2 = new Replica("Привет, Алиса, это Борис.", speaker2, 1);

        tag = new Tag("Совещание");
        tag.setId(1);

        task = new Task(1, "Проверить заметки совещания");
        task.setId(1);

        protocol = new Protocol(1, "Текст протокола совещания");
    }

    @Test
    void testTranscriptReplicaManagement() {
        transcript.addReplica(replica1);
        transcript.addReplica(replica2, 0);

        List<Replica> replicas = (List<Replica>) transcript.getReplicas();
        assertEquals(2, replicas.size());
        assertEquals(replica2, replicas.get(0));
        assertEquals(replica1, replicas.get(1));

        assertEquals("Борис", replicas.get(0).getSpeaker().getName());
        assertEquals("Здравствуйте, это говорит Алиса.", replicas.get(1).getText());

        transcript.removeReplica(0);
        replicas = (List<Replica>) transcript.getReplicas();
        assertEquals(1, replicas.size());
        assertEquals(replica1, replicas.get(0));

        transcript.cutReplica(0);
        replicas = (List<Replica>) transcript.getReplicas();
        assertTrue(replicas.isEmpty());
        assertEquals(replica1, ReplicaBuffer.getReplica());
    }

    @Test
    void testTranscriptWithTagsAndTasks() {
        List<Tag> tags = new ArrayList<>();
        tags.add(tag);
        transcript.setTags(tags);

        transcript.addReplica(replica1);

        List<Tag> retrievedTags = (List<Tag>) transcript.getTags();
        assertEquals(1, retrievedTags.size());
        assertEquals("Совещание", retrievedTags.get(0).getName());
        assertEquals(1, retrievedTags.get(0).getId());

        assertEquals(transcript.getId(), task.getTranscriptId());
        assertEquals("Проверить заметки совещания", task.getDescription());
    }

    @Test
    void testProtocolIntegrationWithTranscript() {
        transcript.addReplica(replica1);
        assertEquals(transcript.getId(), protocol.getTranscriptId());
        assertEquals("Текст протокола совещания", protocol.getText());

        assertEquals("Тестовая транскрипция", transcript.getName());
        List<Replica> replicas = (List<Replica>) transcript.getReplicas();
        assertEquals(1, replicas.size());
    }

    @Test
    void testSpeakerEqualityAndReplicaAssignment() {
        Replica replica3 = new Replica("Другое сообщение", new Speaker("Алиса", image, 1), 1);
        transcript.addReplica(replica1);
        transcript.addReplica(replica3);

        List<Replica> replicas = (List<Replica>) transcript.getReplicas();
        assertEquals(2, replicas.size());
        assertTrue(replicas.get(0).getSpeaker().equals(replicas.get(1).getSpeaker()));
        assertEquals("Алиса", replicas.get(0).getSpeaker().toString());
    }
}