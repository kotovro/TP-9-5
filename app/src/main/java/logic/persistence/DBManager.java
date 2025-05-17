package logic.persistence;

import logic.PlatformDependent;
import logic.persistence.dao.ProtocolDao;
import logic.persistence.dao.SpeakerDao;
import logic.persistence.dao.TaskDao;
import logic.persistence.dao.TranscriptDao;
import logic.video_processing.vosk.VoskRecognizer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBManager {
    private static final String DEFAULT_DB_PATH = "dynamic-resources/saves/saves.db";

    private static Connection connection;
    static {
        try {
            initConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static final TranscriptDao TRANSCRIPT_DAO = new TranscriptDao(connection);
    private static final ProtocolDao PROTOCOL_DAO = new ProtocolDao(connection);
    private static final TaskDao TASK_DAO = new TaskDao(connection);
    private static final SpeakerDao SPEAKER_DAO = new SpeakerDao(connection);

    public static void initConnection() throws Exception {
        String url = "jdbc:sqlite:" + PlatformDependent.getPrefix() + DEFAULT_DB_PATH;
        connection = DriverManager.getConnection(url);
        connection.setAutoCommit(true);
    }

    public static void closeConnection() {
        try {
            if (!connection.isClosed()) connection.close();
        } catch (Exception e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    public static TranscriptDao getTranscriptDao() {
        return TRANSCRIPT_DAO;
    }

    public static SpeakerDao getSpeakerDao() {
        return SPEAKER_DAO;
    }

    public static TaskDao getTaskDao() { return TASK_DAO; }

    public static Path getResourceAsFile(String resourcePath) throws IOException {
        // Получаем поток ресурса
        InputStream is = VoskRecognizer.class.getResourceAsStream(resourcePath);
        if (is == null) {
            throw new FileNotFoundException("Resource not found: " + resourcePath);
        }

        // Создаем временный файл
        Path tempFile = Files.createTempFile("module-resource-",
                resourcePath.substring(resourcePath.lastIndexOf('.')));
        Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
        tempFile.toFile().deleteOnExit();

        return tempFile.toAbsolutePath();
    }
}