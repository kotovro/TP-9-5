package logic.unit_test.general_test;

import logic.general.JsonTaskFiller;
import logic.general.Task;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class JsonTaskFillerTest {

    @Test
    void testCreateTasksListWithValidJson() {
        String json = """
                [
                    {
                        "task": "Написать отчет",
                        "responsible": "Иван",
                        "due_date": "2024-03-20"
                    },
                    {
                        "task": "Провести встречу",
                        "responsible": "Мария",
                        "due_date": "2024-03-21"
                    }
                ]""";
        
        List<Task> tasks = JsonTaskFiller.createTasksList(json, 1);
        
        assertEquals(2, tasks.size());
        assertEquals("Написать отчет. Ответственный: Иван. Дата: 2024-03-20.", tasks.get(0).getDescription());
        assertEquals("Провести встречу. Ответственный: Мария. Дата: 2024-03-21.", tasks.get(1).getDescription());
        assertEquals(1, tasks.get(0).getTranscriptId());
        assertEquals(1, tasks.get(1).getTranscriptId());
    }

    @Test
    void testCreateTasksListWithInvalidJson() {
        String invalidJson = "Invalid JSON";
        List<Task> tasks = JsonTaskFiller.createTasksList(invalidJson, 1);
        assertTrue(tasks.isEmpty());
    }

    @Test
    void testCreateTasksListWithEmptyArray() {
        String emptyJson = "[]";
        List<Task> tasks = JsonTaskFiller.createTasksList(emptyJson, 1);
        assertTrue(tasks.isEmpty());
    }

    @Test
    void testCreateTasksListWithExtraContent() {
        String jsonWithExtra = "Some extra content here... [{\"task\": \"Задача\", \"responsible\": \"Петр\", \"due_date\": \"2024-03-22\"}]";
        List<Task> tasks = JsonTaskFiller.createTasksList(jsonWithExtra, 1);
        
        assertEquals(1, tasks.size());
        assertEquals("Задача. Ответственный: Петр. Дата: 2024-03-22.", tasks.get(0).getDescription());
    }
} 