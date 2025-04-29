package logic.db_tests;

import javafx.scene.image.Image;
import logic.general.Replica;
import logic.general.Speaker;
import logic.general.Transcript;
import logic.persistence.dao.SpeakerDao;
import logic.persistence.dao.TranscriptDao;
import logic.persistence.exception.SpeakerNotFoundException;
import logic.utils.ImageSerializer;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ui.EditController.getImage;

class DaoTest {
    private static final String DEFAULT_DB_PATH = "dynamic-resources/db_examples/saves.db";
    private static final String DB_URL = "jdbc:sqlite:" + DEFAULT_DB_PATH;

    private static Connection connection;
    private SpeakerDao speakerDao;
    private TranscriptDao transcriptDao;

    private Image image = getImage("/images/default_users/undefined.png");
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    @BeforeAll
    static void setup() throws Exception {
        connection = DriverManager.getConnection(DB_URL);

        try (Statement stmt = connection.createStatement();
             var rs = stmt.executeQuery("SELECT 1 FROM sqlite_master WHERE type='table' AND name='speaker'")) {
            if (!rs.next()) {
                throw new IllegalStateException("Таблица 'speaker' не найдена в базе данных");
            }
        }
    }

    @BeforeEach
    void init() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM replica");
            stmt.execute("DELETE FROM transcript");
            stmt.execute("DELETE FROM speaker");
        }

        try (PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO speaker (name, image) VALUES (?, ?)")) {
            pstmt.setString(1, "Test Speaker 1");
            pstmt.setBytes(2, ImageSerializer.imageToByteArray(image));
            pstmt.executeUpdate();

            pstmt.setString(1, "Test Speaker 2");
            pstmt.setBytes(2, ImageSerializer.imageToByteArray(image));
            pstmt.executeUpdate();
        }

        speakerDao = new SpeakerDao(connection);
        transcriptDao = new TranscriptDao(connection);
    }

    @AfterAll
    static void cleanup() throws Exception {
        if (connection != null && !connection.isClosed()) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("DELETE FROM replica");
                stmt.execute("DELETE FROM transcript");
                stmt.execute("DELETE FROM speaker");
            }
            connection.close();
        }
    }

    @Test
    @DisplayName("Добавление нового спикера")
    void testAddSpeaker() {
        Speaker newSpeaker = new Speaker("New Speaker", image, 0);
        speakerDao.addSpeaker(newSpeaker);

        assertTrue(newSpeaker.getId() > 0, "Спикер должен получить ID");
    }

    @Test
    @DisplayName("Попытка получить несуществующего спикера")
    void testGetNonExistentSpeaker() {
        assertThrows(SpeakerNotFoundException.class, () -> {
            speakerDao.getSpeakerById(-999);
        });
    }

    @Test
    @DisplayName("Удаление спикера")
    void testDeleteSpeaker() throws Exception {
        Speaker speaker = new Speaker("To Delete", image, 0);
        speakerDao.addSpeaker(speaker);
        speakerDao.deleteSpeaker(speaker.getId());

        assertThrows(SpeakerNotFoundException.class, () -> {
            speakerDao.getSpeakerById(speaker.getId());
        });
    }

    @Test
    @DisplayName("Получение всех спикеров")
    void testGetAllSpeakers() {
        List<Speaker> speakers = speakerDao.getAllSpeakers();
        assertEquals(2, speakers.size(), "Должны вернуться 2 тестовых спикера");
    }

    @Test
    @DisplayName("Добавление транскрипта с репликами")
    void testAddTranscript() {
        List<Speaker> speakers = speakerDao.getAllSpeakers();
        assertFalse(speakers.isEmpty(), "Должен быть хотя бы один тестовый спикер");

        Speaker speaker = speakers.get(0);
        Replica replica = new Replica();
        replica.setSpeaker(speaker);
        replica.setText("Test replica content");

        Transcript transcript = new Transcript("Test Meeting", new Date());
        transcript.addReplica(replica);

        transcriptDao.addTranscript(transcript);
        List<Transcript> transcripts = transcriptDao.getTranscripts();

        assertEquals(1, transcripts.size());
        assertEquals("Test Meeting", transcripts.get(0).getName());
    }

    @Test
    @DisplayName("Получение списка транскриптов")
    void testGetTranscripts() {
        List<Speaker> speakers = speakerDao.getAllSpeakers();
        Speaker speaker = speakers.get(0);


        Replica replica = new Replica();
        replica.setSpeaker(speaker);
        replica.setText("Test replica content");

        Transcript transcript = new Transcript("Test Meeting", new Date());
        transcript.addReplica(replica);
        transcriptDao.addTranscript(transcript);

        List<Transcript> transcripts = transcriptDao.getTranscripts();

        assertFalse(transcripts.isEmpty());
        Transcript retrieved = transcripts.get(0);
        assertEquals("Test Meeting", retrieved.getName());

    }

    @Test
    @DisplayName("Удаление транскрипта")
    void testDeleteTranscript() {
        List<Speaker> speakers = speakerDao.getAllSpeakers();
        Speaker speaker = speakers.get(0);

        Replica replica = new Replica();
        replica.setSpeaker(speaker);
        replica.setText("Test replica content");

        Transcript transcript = new Transcript("Test Meeting", new Date());
        transcript.addReplica(replica);
        transcriptDao.addTranscript(transcript);

        transcriptDao.deleteTranscript(transcript);

        List<Transcript> transcripts = transcriptDao.getTranscripts();
        assertTrue(transcripts.isEmpty());
    }

    @Test
    @DisplayName("Проверка порядка реплик")
    void testReplicaOrder() {
        List<Speaker> speakers = speakerDao.getAllSpeakers();
        Speaker speaker = speakers.get(0);

        Transcript transcript = new Transcript("Order Test Meeting", new Date());

        Replica replica1 = new Replica();
        replica1.setSpeaker(speaker);
        replica1.setText("First replica");
        transcript.addReplica(replica1);

        Replica replica2 = new Replica();
        replica2.setSpeaker(speaker);
        replica2.setText("Second replica");
        transcript.addReplica(replica2);

        transcriptDao.addTranscript(transcript);

        List<Transcript> transcripts = transcriptDao.getTranscripts();
        assertEquals(1, transcripts.size());
        List<Replica> replicas = (List<Replica>) transcripts.get(0).getReplicas();
        assertEquals(2, replicas.size());
        assertEquals("First replica", replicas.get(0).getText());
        assertEquals("Second replica", replicas.get(1).getText());
    }

    @Test
    @DisplayName("Проверка целостности данных после добавления")
    void testDataIntegrity() throws SQLException {
        String testName = "Integrity Test Speaker";
        Speaker speaker = new Speaker(testName, image, 0);
        speakerDao.addSpeaker(speaker);

        Speaker retrieved = speakerDao.getSpeakerById(speaker.getId());
        assertEquals(testName, retrieved.getName());
        assertEquals(speaker.getId(), retrieved.getId());
    }
}
