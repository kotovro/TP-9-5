package logic.integration_test.persistence_test;

import logic.general.Speaker;
import logic.persistence.DBInitializer;
import logic.persistence.dao.SpeakerDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
            assertEquals(10, count, "Ten speakers should be inserted");
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name, id FROM speaker WHERE name = 'Соня'")) {

            assertTrue(rs.next(), "Speaker 'Соня' should exist");
            assertEquals("Соня", rs.getString("name"), "Speaker name should be 'Соня'");
            assertEquals(5, rs.getInt("id"), "Speaker ID should be 5");
        }
    }

    @Test
    void testSpeakerDaoIntegration() throws SQLException {
        DBInitializer.reinitDB();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH)) {
            SpeakerDao speakerDao = new SpeakerDao(conn);
            List<Speaker> speakers = speakerDao.getAllSpeakers();

            assertEquals(10, speakers.size(), "Ten speakers should be retrieved");

            boolean foundSonya = speakers.stream()
                    .anyMatch(speaker -> "Соня".equals(speaker.getName()) && speaker.getId() == 5);
            assertTrue(foundSonya, "Speaker 'Соня' with ID 5 should exist");
        }
    }

    @Test
    void testAddNewSpeaker() throws SQLException {
        DBInitializer.reinitDB();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH)) {
            SpeakerDao speakerDao = new SpeakerDao(conn);
            Speaker newSpeaker = new Speaker("Тест", DBInitializer.getImage("/images/default_users/undefined.png"), -1);
            speakerDao.addSpeaker(newSpeaker);

            List<Speaker> speakers = speakerDao.getAllSpeakers();
            assertEquals(11, speakers.size(), "Eleven speakers should exist after adding one");

            boolean foundTest = speakers.stream()
                    .anyMatch(speaker -> "Тест".equals(speaker.getName()));
            assertTrue(foundTest, "New speaker 'Тест' should exist");
        }
    }
}