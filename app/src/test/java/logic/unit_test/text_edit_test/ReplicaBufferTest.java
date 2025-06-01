package logic.unit_test.text_edit_test;

import logic.general.Replica;
import logic.text_edit.ReplicaBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ReplicaBufferTest {
    private Replica replica;

    @BeforeEach
    void setUp() {
        ReplicaBuffer.clear();
        replica = mock(Replica.class);
    }

    @Test
    @DisplayName("Проверка пустого буфера")
    void testIsEmptyInitially() {
        assertTrue(ReplicaBuffer.isEmpty(), "Буфер должен быть пустым изначально");
    }

    @Test
    @DisplayName("Проверка установки и получения реплики")
    void testSetAndGetReplica() {
        ReplicaBuffer.setReplica(replica);
        assertFalse(ReplicaBuffer.isEmpty(), "Буфер не должен быть пустым после установки");
        assertEquals(replica, ReplicaBuffer.getReplica(), "Полученная реплика должна совпадать");
    }

    @Test
    @DisplayName("Проверка очистки буфера")
    void testClear() {
        ReplicaBuffer.setReplica(replica);
        ReplicaBuffer.clear();
        assertTrue(ReplicaBuffer.isEmpty(), "Буфер должен быть пустым после очистки");
        assertNull(ReplicaBuffer.getReplica(), "Реплика должна быть null после очистки");
    }
}
