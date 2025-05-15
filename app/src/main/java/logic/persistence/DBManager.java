package logic.persistence;

import logic.persistence.dao.SpeakerDao;
import logic.persistence.dao.TranscriptDao;
import logic.vosk.VoskRecognizer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBManager {
    private static final String DEFAULT_DB_PATH = VoskRecognizer.class.getResource("/saves/saves.db").toString();

    private static String getDBPath() {
        try {
            return getResourceAsFile("/saves/saves.db").toAbsolutePath().toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Connection connection;
    static {
        try {
            initConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static final TranscriptDao TRANSCRIPT_DAO = new TranscriptDao(connection);
    private static final SpeakerDao SPEAKER_DAO = new SpeakerDao(connection);

    public static void initConnection() throws Exception {
        String url = "jdbc:sqlite::resource:" + DEFAULT_DB_PATH;
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