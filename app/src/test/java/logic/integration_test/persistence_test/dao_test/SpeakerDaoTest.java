package logic.integration_test.persistence_test.dao_test;

import logic.general.Speaker;
import logic.persistence.DBInitializer;
import logic.persistence.dao.SpeakerDao;
import logic.persistence.exception.SpeakerNotFoundException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static logic.persistence.DBInitializer.getImage;
import static org.junit.jupiter.api.Assertions.*;

public class SpeakerDaoTest {
    private static final String DB_PATH = "dynamic-resources/saves/saves.db";
    private Connection connection;
    private SpeakerDao speakerDao;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
        speakerDao = new SpeakerDao(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    void testAddSpeaker() {
        Speaker speaker = new Speaker("Test Speaker", getImage("/images/default_users/undefined.png"), 0);
        speakerDao.addSpeaker(speaker);

        assertTrue(speaker.getId() > 0, "Speaker ID should be set after insertion");
        var speakers = speakerDao.getAllSpeakers();
        assertTrue(speakers.stream().anyMatch(s -> "Test Speaker".equals(s.getName())), "Speaker should be added");
    }

    //TODO: ждет реализации
//    @Test
//    void testDeleteSpeaker() throws SQLException {
//        int speakerId;
//        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO speaker (name, image) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
//            stmt.setString(1, "Test Speaker");
//            stmt.setBytes(2, null); // Image can be NULL in the database
//            stmt.executeUpdate();
//            try (ResultSet rs = stmt.getGeneratedKeys()) {
//                assertTrue(rs.next(), "Generated key should be available");
//                speakerId = rs.getInt(1);
//            }
//        }
//
//        speakerDao.deleteSpeaker(speakerId);
//        assertThrows(SpeakerNotFoundException.class, () -> speakerDao.getSpeakerById(speakerId), "Speaker should be deleted");
//    }

    @Test
    void testGetSpeakerById() {
        Speaker speaker = new Speaker("Test Speaker", getImage("/images/default_users/undefined.png"), 0);
        speakerDao.addSpeaker(speaker);

        Speaker retrieved = speakerDao.getSpeakerById(speaker.getId());
        assertEquals("Test Speaker", retrieved.getName(), "Speaker name should match");
        assertEquals(speaker.getId(), retrieved.getId(), "Speaker ID should match");
    }

    @Test
    void testGetSpeakerByIdNotFound() {
        assertThrows(SpeakerNotFoundException.class, () -> speakerDao.getSpeakerById(999), "Should throw SpeakerNotFoundException");
    }

    @Test
    void testGetAllSpeakers() {
        var before = speakerDao.getAllSpeakers();
        int initialCount = before.size();

        Speaker speaker1 = new Speaker("Speaker 1", getImage("/images/default_users/undefined.png"), 0);
        Speaker speaker2 = new Speaker("Speaker 2", getImage("/images/default_users/undefined.png"), 0);
        speakerDao.addSpeaker(speaker1);
        speakerDao.addSpeaker(speaker2);

        var after = speakerDao.getAllSpeakers();
        assertEquals(initialCount + 2, after.size(), "Speaker count should increase by 2 after adding");

        assertTrue(after.stream().anyMatch(s -> "Speaker 1".equals(s.getName())), "Speaker 1 should exist");
        assertTrue(after.stream().anyMatch(s -> "Speaker 2".equals(s.getName())), "Speaker 2 should exist");
    }
}