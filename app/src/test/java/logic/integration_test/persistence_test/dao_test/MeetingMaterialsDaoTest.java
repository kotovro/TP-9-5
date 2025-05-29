package logic.integration_test.persistence_test.dao_test;

import logic.general.MeetingMaterials;
import logic.general.Protocol;
import logic.general.Replica;
import logic.general.Speaker;
import logic.general.Task;
import logic.general.Transcript;
import logic.persistence.dao.MeetingMaterialsDao;
import logic.persistence.dao.ProtocolDao;
import logic.persistence.dao.SpeakerDao;
import logic.persistence.dao.TaskDao;
import logic.persistence.dao.TranscriptDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static logic.persistence.DBInitializer.getImage;
import static org.junit.jupiter.api.Assertions.*;

public class MeetingMaterialsDaoTest {
    private static final String DB_PATH = "dynamic-resources/saves/saves.db";
    private Connection connection;
    private MeetingMaterialsDao meetingMaterialsDao;
    private TranscriptDao transcriptDao;
    private TaskDao taskDao;
    private ProtocolDao protocolDao;
    private SpeakerDao speakerDao;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private Speaker testSpeaker;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
        transcriptDao = new TranscriptDao(connection);
        taskDao = new TaskDao(connection);
        protocolDao = new ProtocolDao(connection);
        speakerDao = new SpeakerDao(connection);
        meetingMaterialsDao = new MeetingMaterialsDao(connection, transcriptDao, taskDao, protocolDao);

        try (var stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM task");
            stmt.execute("DELETE FROM protocol");
            stmt.execute("DELETE FROM transcript_tag");
            stmt.execute("DELETE FROM replica");
            stmt.execute("DELETE FROM transcript");
            stmt.execute("DELETE FROM speaker");
        }

        testSpeaker = new Speaker("Test Speaker", getImage("/images/default_users/undefined.png"), 0);
        speakerDao.addSpeaker(testSpeaker);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            try (var stmt = connection.createStatement()) {
                stmt.execute("DELETE FROM task");
                stmt.execute("DELETE FROM protocol");
                stmt.execute("DELETE FROM transcript_tag");
                stmt.execute("DELETE FROM replica");
                stmt.execute("DELETE FROM transcript");
                stmt.execute("DELETE FROM speaker");
            }
            connection.close();
        }
    }

    @Test
    void testGetAllMeetingMaterials_Success() throws Exception {
        Transcript transcript1 = new Transcript("Transcript 1", new Date());
        List<Replica> replicas1 = new ArrayList<>();
        replicas1.add(new Replica("Test content 1", testSpeaker, 1));
        transcript1.setReplicas(replicas1);
        transcriptDao.addTranscript(transcript1);

        Transcript transcript2 = new Transcript("Transcript 2", new Date());
        List<Replica> replicas2 = new ArrayList<>();
        replicas2.add(new Replica("Test content 2", testSpeaker, 1));
        transcript2.setReplicas(replicas2);
        transcriptDao.addTranscript(transcript2);

        Protocol protocol1 = new Protocol(transcript1.getId(), "Conclusion 1");
        protocolDao.addProtocol(protocol1);

        Task task1 = new Task(transcript1.getId(), "Task 1");
        Task task2 = new Task(transcript1.getId(), "Task 2");
        taskDao.addTask(task1);
        taskDao.addTask(task2);

        List<MeetingMaterials> result = meetingMaterialsDao.getAllMeetingMaterials();

        assertNotNull(result);
        assertEquals(2, result.size(), "Should return two MeetingMaterials");

        MeetingMaterials materials1 = result.stream()
                .filter(m -> m.getTranscript().getId() == transcript1.getId())
                .findFirst()
                .orElseThrow(() -> new AssertionError("Materials for Transcript 1 not found"));
        assertEquals(transcript1.getName(), materials1.getTranscript().getName(), "Transcript name should match");
        assertTrue(materials1.getProtocol().isPresent(), "Protocol should be present for Transcript 1");
        assertEquals("Conclusion 1", materials1.getProtocol().get().getText(), "Protocol text should match");
        assertEquals(2, materials1.getTasks().size(), "Should have two tasks for Transcript 1");
        assertTrue(materials1.getTasks().stream().anyMatch(t -> "Task 1".equals(t.getDescription())), "Task 1 should exist");
        assertTrue(materials1.getTasks().stream().anyMatch(t -> "Task 2".equals(t.getDescription())), "Task 2 should exist");

        MeetingMaterials materials2 = result.stream()
                .filter(m -> m.getTranscript().getId() == transcript2.getId())
                .findFirst()
                .orElseThrow(() -> new AssertionError("Materials for Transcript 2 not found"));
        assertEquals(transcript2.getName(), materials2.getTranscript().getName(), "Transcript name should match");
        assertFalse(materials2.getProtocol().isPresent(), "Protocol should not be present for Transcript 2");
        assertTrue(materials2.getTasks().isEmpty(), "No tasks should exist for Transcript 2");
    }

    @Test
    void testGetAllMeetingMaterials_EmptyDatabase() throws Exception {
        List<MeetingMaterials> result = meetingMaterialsDao.getAllMeetingMaterials();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Should return empty list when no transcripts exist");
    }

    @Test
    void testGetAllMeetingMaterials_TranscriptWithNoProtocolOrTasks() throws Exception {
        Transcript transcript = new Transcript("Transcript No Data", new Date());
        List<Replica> replicas = new ArrayList<>();
        replicas.add(new Replica("Test content", testSpeaker, 1));
        transcript.setReplicas(replicas);
        transcriptDao.addTranscript(transcript);

        List<MeetingMaterials> result = meetingMaterialsDao.getAllMeetingMaterials();

        assertNotNull(result);
        assertEquals(1, result.size(), "Should return one MeetingMaterials");
        MeetingMaterials materials = result.get(0);
        assertEquals(transcript.getName(), materials.getTranscript().getName(), "Transcript name should match");
        assertFalse(materials.getProtocol().isPresent(), "No protocol should be present");
        assertTrue(materials.getTasks().isEmpty(), "No tasks should be present");
    }

    @Test
    void testGetAllMeetingMaterials_DatabaseError() throws SQLException {
        connection.close();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            meetingMaterialsDao.getAllMeetingMaterials();
        });
        assertEquals("Error retrieving meeting materials", exception.getMessage(), "Exception message should match");
        assertNotNull(exception.getCause(), "Exception should have a cause");
    }
}