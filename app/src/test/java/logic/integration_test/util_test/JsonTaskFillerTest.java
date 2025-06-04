package logic.integration_test.util_test;

import com.fasterxml.jackson.core.JsonProcessingException;

import logic.general.Task;
import logic.utils.JsonTaskFiller;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonTaskFillerTest {

    @Test
    void testCreateTasksList_ValidJson(){
        String json = "[{\"task\": \"Complete report\", \"responsible\": \"John\", \"due_date\": \"01-01-2025\"}, " +
                      "{\"task\": \"Review slides\", \"responsible\": \"Jane\", \"due_date\": \"02-01-2025\"}]";
        int transcriptId = 1;
        String expectedDesc1 = "Complete report. Дата: 01-01-2025.";
        String expectedDesc2 = "Review slides. Дата: 02-01-2025.";
        List<Task> tasks = JsonTaskFiller.createTasksList(json, transcriptId);

        assertNotNull(tasks);
        assertEquals(2, tasks.size(), "Should return two tasks");
        assertEquals(transcriptId, tasks.get(0).getTranscriptId(), "Transcript ID should match");
        assertEquals(expectedDesc1, tasks.get(0).getDescription(), "Task 1 description should match");
        assertEquals(transcriptId, tasks.get(1).getTranscriptId(), "Transcript ID should match");
        assertEquals(expectedDesc2, tasks.get(1).getDescription(), "Task 2 description should match");
    }

    @Test
    void testCreateTasksList_EmptyJsonArray() {
        String json = "[]";
        int transcriptId = 1;
        List<Task> tasks = JsonTaskFiller.createTasksList(json, transcriptId);

        assertNotNull(tasks);
        assertTrue(tasks.isEmpty(), "Should return empty list for empty JSON array");
    }

    @Test
    void testCreateTasksList_InvalidJson() {
        String json = "Invalid JSON";
        int transcriptId = 1;
        List<Task> tasks = JsonTaskFiller.createTasksList(json, transcriptId);

        assertNotNull(tasks);
        assertTrue(tasks.isEmpty(), "Should return empty list for invalid JSON");
    }

    @Test
    void testCreateTasksList_MissingArray() {
        String json = "{\"task\": \"Complete report\"}";
        int transcriptId = 1;
        List<Task> tasks = JsonTaskFiller.createTasksList(json, transcriptId);

        assertNotNull(tasks);
        assertTrue(tasks.isEmpty(), "Should return empty list when JSON array is missing");
    }

    @Test
    void testCreateTasksList_PartialFields() {
        String json = "[{\"task\": \"Complete report\", \"due_date\": \"01-01-2025\"}]";
        int transcriptId = 1;
        String expectedDesc = "Complete report. Дата: 01-01-2025.";
        List<Task> tasks = JsonTaskFiller.createTasksList(json, transcriptId);

        assertNotNull(tasks);
        assertEquals(1, tasks.size(), "Should return one task");
        assertEquals(transcriptId, tasks.get(0).getTranscriptId(), "Transcript ID should match");
        assertEquals(expectedDesc, tasks.get(0).getDescription(), "Task description should match");
    }
}