package logic.unit_test.general_test;

import javafx.util.Pair;
import logic.general.Replica;
import logic.general.Speaker;
import logic.general.Tag;
import logic.general.Transcript;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TranscriptTest {

    @Mock
    private Replica replica;

    @Mock
    private Speaker speaker;

    @Mock
    private Tag tag;

    private Transcript transcript;
    private Date date;

    @BeforeEach
    void setUp() {
        date = new Date();
        transcript = new Transcript("Test Transcript", date);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals("Test Transcript", transcript.getName(), "Name should be set correctly");
        assertEquals(date, transcript.getDate(), "Date should be set correctly");
        assertEquals(-1, transcript.getId(), "ID should default to -1");
        assertTrue(transcript.getReplicas().iterator().hasNext() == false, "Replicas should be empty");
        assertTrue(transcript.getTags().iterator().hasNext() == false, "Tags should be empty");
    }

    @Test
    void testSetAndGetId() {
        transcript.setId(1);
        assertEquals(1, transcript.getId(), "ID should be updated correctly");
    }

    @Test
    void testSetAndGetName() {
        transcript.setName("New Transcript");
        assertEquals("New Transcript", transcript.getName(), "Name should be updated correctly");
    }

    @Test
    void testAddReplica() {
        transcript.addReplica(replica);
        List<Replica> replicas = (List<Replica>) transcript.getReplicas();
        assertEquals(1, replicas.size(), "Should have one replica");
        assertEquals(replica, replicas.get(0), "Replica should be added correctly");
    }

    @Test
    void testAddReplicaAtIndex() {
        Replica replica2 = mock(Replica.class);
        transcript.addReplica(replica);
        transcript.addReplica(replica2, 0);
        List<Replica> replicas = (List<Replica>) transcript.getReplicas();
        assertEquals(2, replicas.size(), "Should have two replicas");
        assertEquals(replica2, replicas.get(0), "Replica2 should be at index 0");
        assertEquals(replica, replicas.get(1), "Replica should be at index 1");
    }

    @Test
    void testRemoveReplicaByIndex() {
        transcript.addReplica(replica);
        transcript.removeReplica(0);
        List<Replica> replicas = (List<Replica>) transcript.getReplicas();
        assertTrue(replicas.isEmpty(), "Replica should be removed");
    }

    @Test
    void testRemoveReplicaByObject() {
        transcript.addReplica(replica);
        transcript.removeReplica(replica);
        List<Replica> replicas = (List<Replica>) transcript.getReplicas();
        assertTrue(replicas.isEmpty(), "Replica should be removed");
    }

    @Test
    void testCutReplica() {
        transcript.addReplica(replica);
        transcript.cutReplica(0);
        List<Replica> replicas = (List<Replica>) transcript.getReplicas();
        assertTrue(replicas.isEmpty(), "Replica should be removed");
        assertEquals(replica, ReplicaBuffer.getReplica(), "Replica should be set in ReplicaBuffer");
    }

    @Test
    void testSetAndGetReplicas() {
        List<Replica> newReplicas = new ArrayList<>();
        newReplicas.add(replica);
        transcript.setReplicas(newReplicas);
        List<Replica> replicas = (List<Replica>) transcript.getReplicas();
        assertEquals(1, replicas.size(), "Should have one replica");
        assertEquals(replica, replicas.get(0), "Replica should be set correctly");
    }

    @Test
    void testSetAndGetTags() {
        List<Tag> newTags = new ArrayList<>();
        newTags.add(tag);
        transcript.setTags(newTags);
        List<Tag> tags = (List<Tag>) transcript.getTags();
        assertEquals(1, tags.size(), "Should have one tag");
        assertEquals(tag, tags.get(0), "Tag should be set correctly");
    }

    @Test
    void testFindTextNoMatch() {
        when(replica.getText()).thenReturn("Sample text");
        transcript.addReplica(replica);
        List<Pair<Replica, Integer>> results = transcript.findText(transcript, "Nonexistent");
        assertEquals(1, results.size(), "Should return one result");
        assertEquals(-1, results.get(0).getValue(), "Should return -1 for no match");
    }

    @Test
    void testFindTextSingleMatch() {
        when(replica.getText()).thenReturn("Sample text");
        transcript.addReplica(replica);
        List<Pair<Replica, Integer>> results = transcript.findText(transcript, "text");
        assertEquals(1, results.size(), "Should return one result");
        assertEquals(7, results.get(0).getValue(), "Should find match at index 7");
    }
}
