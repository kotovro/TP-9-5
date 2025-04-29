package logic.text_tests;

import logic.general.Replica;
import logic.general.Speaker;
import logic.text_edit.ReplicaBuffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static ui.EditController.getImage;

class ReplicaBufferTest {
    private Replica testReplica;

    @BeforeEach
    void setUp() {
        Speaker speaker = new Speaker("Ivan", getImage("/images/default_users/undefined.png"), 0);
        testReplica = new Replica("test", speaker);
        ReplicaBuffer.clear();
    }

    @AfterEach
    void tearDown() {
        ReplicaBuffer.clear();
    }

    @Test
    void isEmpty_shouldReturnTrueWhenEmpty() {
        assertTrue(ReplicaBuffer.isEmpty());
    }

    @Test
    void isEmpty_shouldReturnFalseWhenNotEmpty() {
        ReplicaBuffer.setReplica(testReplica);
        assertFalse(ReplicaBuffer.isEmpty());
    }

    @Test
    void setAndGetReplica_shouldWorkCorrectly() {
        ReplicaBuffer.setReplica(testReplica);
        assertEquals(testReplica, ReplicaBuffer.getReplica());
    }

    @Test
    void clear_shouldEmptyBuffer() {
        ReplicaBuffer.setReplica(testReplica);
        ReplicaBuffer.clear();
        assertTrue(ReplicaBuffer.isEmpty());
    }
}
