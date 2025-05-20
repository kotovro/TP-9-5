package logic.integration_test.persistence_test.dao_test;

import logic.general.Protocol;
import logic.persistence.DBInitializer;
import logic.persistence.dao.ProtocolDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class ProtocolDaoTest {
    private static final String DB_PATH = "dynamic-resources/saves/saves.db";
    private Connection connection;
    private ProtocolDao protocolDao;

    @BeforeEach
    void setUp() throws IOException, SQLException {
        DBInitializer.deleteIfExist();
        DBInitializer.reinitDB();

        connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
        protocolDao = new ProtocolDao(connection);

        // Insert a test transcript for foreign key constraint
        try (var stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO transcript (name, date) VALUES ('Test Transcript', '01-01-2025')");
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        DBInitializer.deleteIfExist();
    }

    @Test
    void testAddProtocol() throws SQLException {
        Protocol protocol = new Protocol(1, "Test conclusion");
        protocolDao.addProtocol(protocol);

        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery("SELECT conclusion, transcript_id FROM protocol WHERE transcript_id = 1")) {
            assertTrue(rs.next(), "Protocol should exist");
            assertEquals("Test conclusion", rs.getString("conclusion"));
            assertEquals(1, rs.getInt("transcript_id"));
        }
    }

    @Test
    void testAddProtocolInvalidTranscript() {
        Protocol protocol = new Protocol(-1, "Test conclusion");
        assertThrows(Exception.class, () -> protocolDao.addProtocol(protocol),
                "Should throw TranscriptDoesNotExistException for invalid transcript ID");
    }

    @Test
    void testUpdateProtocol() throws SQLException {
        try (var stmt = connection.prepareStatement("INSERT INTO protocol (conclusion, transcript_id) VALUES (?, ?)")) {
            stmt.setString(1, "Initial conclusion");
            stmt.setInt(2, 1);
            stmt.executeUpdate();
        }

        Protocol updatedProtocol = new Protocol(1, "Updated conclusion");
        protocolDao.updateProtocol(updatedProtocol);

        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery("SELECT conclusion FROM protocol WHERE transcript_id = 1")) {
            assertTrue(rs.next(), "Protocol should exist");
            assertEquals("Updated conclusion", rs.getString("conclusion"));
        }
    }

    @Test
    void testDeleteProtocol() throws SQLException {
        try (var stmt = connection.prepareStatement("INSERT INTO protocol (conclusion, transcript_id) VALUES (?, ?)")) {
            stmt.setString(1, "Test conclusion");
            stmt.setInt(2, 1);
            stmt.executeUpdate();
        }

        Protocol protocol = new Protocol(1, "Test conclusion");
        protocolDao.deleteProtocol(protocol);

        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM protocol WHERE transcript_id = 1")) {
            assertTrue(rs.next());
            assertEquals(0, rs.getInt("count"), "Protocol should be deleted");
        }
    }

    @Test
    void testGetAllProtocols() throws SQLException {
        try (var stmt = connection.prepareStatement("INSERT INTO protocol (conclusion, transcript_id) VALUES (?, ?)")) {
            stmt.setString(1, "Conclusion 1");
            stmt.setInt(2, 1);
            stmt.executeUpdate();
        }

        var protocols = protocolDao.getAllProtocols();
        assertEquals(1, protocols.size(), "One protocol should be retrieved");
        assertEquals("Conclusion 1", protocols.get(0).getText());
        assertEquals(1, protocols.get(0).getTranscriptId());
    }
}