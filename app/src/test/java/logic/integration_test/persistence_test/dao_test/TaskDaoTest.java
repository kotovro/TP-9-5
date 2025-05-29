package logic.integration_test.persistence_test.dao_test;

import logic.general.Task;

import logic.persistence.dao.TaskDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class TaskDaoTest {
    private static final String DB_PATH = "dynamic-resources/saves/saves.db";
    private Connection connection;
    private TaskDao taskDao;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
        taskDao = new TaskDao(connection);

        try (var stmt = connection.createStatement()) {
            // Clear tables to ensure clean state
            stmt.execute("DELETE FROM task");
            stmt.execute("DELETE FROM transcript");
            // Insert test transcript
            stmt.execute("INSERT INTO transcript (name, date) VALUES ('Test Transcript', '01-01-2025')");
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            try (var stmt = connection.createStatement()) {
                // Clean up tables
                stmt.execute("DELETE FROM task");
                stmt.execute("DELETE FROM transcript");
            }
            connection.close();
        }
    }

    @Test
    void testAddTask() throws SQLException {
        Task task = new Task(1, "Test Task");
        taskDao.addTask(task);

        assertTrue(task.getId() > 0, "Task ID should be set after insertion");
        var tasks = taskDao.getTasksByTranscriptId(1);
        assertEquals(1, tasks.size(), "One task should be added");
        assertEquals("Test Task", tasks.get(0).getDescription());
    }

    @Test
    void testDeleteTask() throws SQLException {
        Task task = new Task(1, "Test Task");
        taskDao.addTask(task);

        taskDao.deleteTask(task);
        assertNull(taskDao.getTaskById(task.getId()), "Task should be deleted");
    }

    @Test
    void testUpdateTask() throws SQLException {
        Task task = new Task(1, "Initial Task");
        taskDao.addTask(task);

        task.setDescription("Updated Task");
        taskDao.updateTask(task);

        Task updatedTask = taskDao.getTaskById(task.getId());
        assertEquals("Updated Task", updatedTask.getDescription(), "Task description should be updated");
        assertEquals(1, updatedTask.getTranscriptId(), "Transcript ID should remain unchanged");
    }

    @Test
    void testGetTasksByTranscriptId() throws SQLException {
        Task task1 = new Task(1, "Task 1");
        Task task2 = new Task(1, "Task 2");
        taskDao.addTask(task1);
        taskDao.addTask(task2);

        var tasks = taskDao.getTasksByTranscriptId(1);
        assertEquals(2, tasks.size(), "Two tasks should be retrieved");
        assertTrue(tasks.stream().anyMatch(t -> "Task 1".equals(t.getDescription())), "Task 1 should exist");
        assertTrue(tasks.stream().anyMatch(t -> "Task 2".equals(t.getDescription())), "Task 2 should exist");
    }

    @Test
    void testGetTaskById() throws SQLException {
        Task task = new Task(1, "Test Task");
        taskDao.addTask(task);

        Task retrieved = taskDao.getTaskById(task.getId());
        assertEquals("Test Task", retrieved.getDescription(), "Task description should match");
        assertEquals(1, retrieved.getTranscriptId(), "Transcript ID should match");
    }

    @Test
    void testGetTaskByIdNotFound() throws SQLException {
        assertNull(taskDao.getTaskById(999), "Non-existent task should return null");
    }
}