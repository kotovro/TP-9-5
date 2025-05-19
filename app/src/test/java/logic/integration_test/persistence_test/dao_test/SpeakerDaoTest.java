package logic.integration_test.persistence_test.dao_test;

import javafx.scene.image.Image;
import logic.general.Speaker;
import logic.persistence.DBInitializer;
import logic.persistence.dao.SpeakerDao;
import logic.persistence.exception.SpeakerNotFoundException;
import ui.EditController;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class SpeakerDaoTest {
    private static final String DB_PATH = "dynamic-resources/saves/saves.db";
    private Connection connection;
    private SpeakerDao speakerDao;

    @BeforeEach
    void setUp() throws IOException, SQLException {
        DBInitializer.deleteIfExist();
        DBInitializer.reinitDB();

        connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
        speakerDao = new SpeakerDao(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        DBInitializer.deleteIfExist();
    }

    @Test
    void testAddSpeaker() {
        Speaker speaker = new Speaker("Test Speaker", EditController.getImage("/images/default_users/undefined.png"), 0);
        speakerDao.addSpeaker(speaker);

        assertTrue(speaker.getId() > 0, "Speaker ID should be set after insertion");
        var speakers = speakerDao.getAllSpeakers();
        assertTrue(speakers.stream().anyMatch(s -> "Test Speaker".equals(s.getName())), "Speaker should be added");
    }

    @Test
    void testDeleteSpeaker() throws SQLException {
        try (var stmt = connection.prepareStatement("INSERT INTO speaker (name, image) VALUES (?, ?)")) {
            stmt.setString(1, "Test Speaker");
            stmt.setBytes(2, null); // Image can be NULL in the database
            stmt.executeUpdate();
        }

        speakerDao.deleteSpeaker(8); // ID 8, as initial speakers have IDs 1-7
        assertThrows(SpeakerNotFoundException.class, () -> speakerDao.getSpeakerById(8), "Speaker should be deleted");
    }

    @Test
    void testGetSpeakerById() {
        Speaker speaker = new Speaker("Test Speaker", EditController.getImage("/images/default_users/undefined.png"), 0);
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
        Speaker speaker1 = new Speaker("Speaker 1", EditController.getImage("/images/default_users/undefined.png"), 0);
        Speaker speaker2 = new Speaker("Speaker 2", EditController.getImage("/images/default_users/undefined.png"), 0);
        speakerDao.addSpeaker(speaker1);
        speakerDao.addSpeaker(speaker2);

        var speakers = speakerDao.getAllSpeakers();
        assertEquals(9, speakers.size(), "Nine speakers (7 initial + 2 new) should be retrieved");
        assertTrue(speakers.stream().anyMatch(s -> "Speaker 1".equals(s.getName())), "Speaker 1 should exist");
        assertTrue(speakers.stream().anyMatch(s -> "Speaker 2".equals(s.getName())), "Speaker 2 should exist");
    }
}