package logic.unit_test.general_test;

import logic.general.MeetingMaterials;
import logic.general.Protocol;
import logic.general.Task;
import logic.general.Transcript;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeetingMaterialsTest {

    @Mock
    private Transcript transcript;

    @Mock
    private Protocol protocol;

    private List<Task> tasks;
    private MeetingMaterials meetingMaterials;

    @BeforeEach
    void setUp() {
        tasks = new ArrayList<>();
        meetingMaterials = new MeetingMaterials(transcript, Optional.of(protocol), tasks);
    }

    @Test
    void testConstructorWithValidInputs() {
        Transcript testTranscript = mock(Transcript.class);
        Protocol testProtocol = mock(Protocol.class);
        List<Task> testTasks = new ArrayList<>();
        testTasks.add(new Task(1, "Test Task"));
        MeetingMaterials materials = new MeetingMaterials(testTranscript, Optional.of(testProtocol), testTasks);

        assertEquals(testTranscript, materials.getTranscript());
        assertEquals(Optional.of(testProtocol), materials.getProtocol());
        assertEquals(testTasks, materials.getTasks());
    }

    @Test
    void testConstructorWithEmptyProtocol() {
        Transcript testTranscript = mock(Transcript.class);
        List<Task> testTasks = new ArrayList<>();
        MeetingMaterials materials = new MeetingMaterials(testTranscript, Optional.empty(), testTasks);

        assertEquals(testTranscript, materials.getTranscript());
        assertEquals(Optional.empty(), materials.getProtocol());
        assertEquals(testTasks, materials.getTasks());
    }

    @Test
    void testGetTranscript() {
        assertEquals(transcript, meetingMaterials.getTranscript());
    }

    @Test
    void testGetProtocol() {
        assertEquals(Optional.of(protocol), meetingMaterials.getProtocol());
    }

    @Test
    void testGetTasks() {
        Task task = new Task(1, "Sample Task");
        tasks.add(task);
        List<Task> retrievedTasks = meetingMaterials.getTasks();

        assertEquals(1, retrievedTasks.size());
        assertEquals(task, retrievedTasks.get(0));
    }

    @Test
    void testGetTasksEmptyList() {
        List<Task> retrievedTasks = meetingMaterials.getTasks();
        assertTrue(retrievedTasks.isEmpty());
    }

    @Test
    void testGetTasksReturnsSameList() {
        Task task = new Task(1, "Sample Task");
        tasks.add(task);
        List<Task> originalTasks = meetingMaterials.getTasks();
        originalTasks.add(new Task(2, "Another Task"));
        List<Task> retrievedTasks = meetingMaterials.getTasks();

        assertEquals(2, retrievedTasks.size());
        assertEquals(originalTasks, retrievedTasks); // Проверяем, что возвращается тот же список
    }
}