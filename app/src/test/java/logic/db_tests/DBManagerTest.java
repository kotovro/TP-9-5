package logic.db_tests;

import logic.general.Speaker;
import logic.persistence.DBManager;
import logic.persistence.dao.SpeakerDao;
import logic.persistence.dao.TranscriptDao;
import org.junit.jupiter.api.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ui.EditController.getImage;

class DBManagerTest {
    private static final String TEST_DB_PATH = "app\\dynamic-resources\\db_examples\\saves.db";

    @BeforeAll
    static void setUp() throws Exception {
        Files.deleteIfExists(Paths.get(TEST_DB_PATH));
        System.setProperty("DEFAULT_DB_PATH", TEST_DB_PATH);
        DBManager.getConnection();
    }

    @AfterAll
    static void tearDown() throws Exception {
        DBManager.closeConnection();
        Files.deleteIfExists(Paths.get(TEST_DB_PATH));
    }

    @Test
    @DisplayName("Проверка соединения с БД")
    void testConnectionInitialization() throws SQLException {
        Connection connection = DBManager.getConnection();
        assertNotNull(connection, "Соединение с БД не должно быть null");
        assertFalse(connection.isClosed(), "Соединение должно быть открытым");
    }

    @Test
    @DisplayName("Дефолтное добавление спикера")
    void testDefaultSpeakersAdded() {
        SpeakerDao speakerDao = DBManager.getSpeakerDao();
        assertNotNull(speakerDao, "SpeakerDao не должен быть null");

        var speakers = speakerDao.getAllSpeakers();
        assertNotNull(speakers, "Список спикеров не должен быть null");
        assertFalse(speakers.isEmpty(), "Список спикеров не должен быть пустым");
    }

    @Test
    @DisplayName("Проверка добавления/удаления спикера")
    void testAddAndDeleteSpeaker() {
        SpeakerDao speakerDao = DBManager.getSpeakerDao();
        Speaker testSpeaker = new Speaker("Test Speaker", getImage("/images/default_users/undefined.png"), 100);

        assertDoesNotThrow(() -> speakerDao.addSpeaker(testSpeaker),
                "Добавление спикера не должно вызывать исключений");

        assertDoesNotThrow(() -> speakerDao.deleteSpeaker(testSpeaker.getId()),
                "Удаление спикера не должно вызывать исключений");
    }

    @Test
    @DisplayName("Проверка получения DAO")
    void testGetDaos() {
        assertNotNull(DBManager.getSpeakerDao(), "SpeakerDao должен быть доступен");
        assertNotNull(DBManager.getTranscriptDao(), "TranscriptDao должен быть доступен");
    }

    @Test
    @DisplayName("Проверка закрытия соединения")
    void testCloseConnection() throws SQLException {
        DBManager.closeConnection();

        Connection connection = DBManager.getConnection();
        assertTrue(connection.isClosed(), "Соединение должно быть закрыто");
    }
}