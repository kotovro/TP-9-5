package logic.unit_test.general_test;

import logic.general.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task(1, "Sample task description");
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(1, task.getTranscriptId());
        assertEquals("Sample task description", task.getDescription());
        assertEquals(0, task.getId());
    }

    @Test
    void testSetAndGetId() {
        task.setId(2);
        assertEquals(2, task.getId());
    }
}
