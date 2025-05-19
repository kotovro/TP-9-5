package logic.integration_test.persistence_test;

import logic.general.Speaker;
import logic.persistence.DBInitializer;
import logic.persistence.dao.SpeakerDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ui.EditController.getImage;

public class DBInitializerTest {
    private static final String DB_PATH = "dynamic-resources/saves/saves.db";

    @BeforeEach
    void setUp() {
        DBInitializer.deleteIfExist();
    }

    @AfterEach
    void tearDown() {
        DBInitializer.deleteIfExist();
    }

    @Test
    void testDatabaseConnection() throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH)) {
            assertNotNull(conn, "Connection should not be null");
            assertFalse(conn.isClosed(), "Connection should be open");
        }
    }

    @Test
    void testDatabaseCreation() throws SQLException {
        DBInitializer.reinitDB();

        assertTrue(Files.exists(Paths.get(DB_PATH)), "Database file should exist");

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
             ResultSet rs = conn.getMetaData().getTables(null, null, null, new String[]{"TABLE"})) {

            int tableCount = 0;
            String[] expectedTables = {"speaker", "tag", "transcript", "replica", "protocol", "task", "transcript_tag"};
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                for (String expectedTable : expectedTables) {
                    if (expectedTable.equalsIgnoreCase(tableName)) {
                        tableCount++;
                        break;
                    }
                }
            }
            assertEquals(expectedTables.length, tableCount, "All expected tables should be created");
        }
    }

    @Test
    void testInitialSpeakersInsertion() throws SQLException {
        DBInitializer.reinitDB();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM speaker")) {

            assertTrue(rs.next(), "ResultSet should have a row");
            int count = rs.getInt("count");
            assertEquals(7, count, "Seven speakers should be inserted");
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name, id FROM speaker WHERE name = 'Соня'")) {

            assertTrue(rs.next(), "Speaker 'Соня' should exist");
            assertEquals("Соня", rs.getString("name"), "Speaker name should be 'Соня'");
            assertEquals(2, rs.getInt("id"), "Speaker ID should be 1");
        }
    }

    @Test
    void testSpeakerDaoIntegration() throws IOException, SQLException {
        DBInitializer.reinitDB();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH)) {
            SpeakerDao speakerDao = new SpeakerDao(conn);
            List<Speaker> speakers = speakerDao.getAllSpeakers();

            assertEquals(7, speakers.size(), "Seven speakers should be retrieved");

            boolean foundSonya = speakers.stream()
                    .anyMatch(speaker -> "Соня".equals(speaker.getName()) && speaker.getId() == 2);
            assertTrue(foundSonya, "Speaker 'Соня' with ID 1 should exist");
        }
    }

    @Test
    void testAddNewSpeaker() throws IOException, SQLException {
        // Run the database initialization
        DBInitializer.reinitDB();

        // Add a new speaker using SpeakerDao
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH)) {
            SpeakerDao speakerDao = new SpeakerDao(conn);
            Speaker newSpeaker = new Speaker("Тест", getImage("/images/default_users/undefined.png"), 8); // Заглушка вместо getImage
            speakerDao.addSpeaker(newSpeaker);

            // Verify the new speaker
            List<Speaker> speakers = speakerDao.getAllSpeakers();
            assertEquals(8, speakers.size(), "Eight speakers should exist after adding one");

            boolean foundTest = speakers.stream()
                    .anyMatch(speaker -> "Тест".equals(speaker.getName()));
            assertTrue(foundTest, "New speaker 'Тест' should exist");
        }
    }
}
