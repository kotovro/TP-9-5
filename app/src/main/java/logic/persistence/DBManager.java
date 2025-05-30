package logic.persistence;

import logic.PlatformDependent;
import logic.persistence.dao.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBManager {
    private static final String DEFAULT_DB_PATH = "dynamic-resources/saves/saves.db";
    private static final String DB_NAME = "saves.db";

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
    private static final TagDao TAG_DAO = new TagDao(connection);
    private static final MeetingMaterialsDao MEETING_MATERIALS_DAO = new MeetingMaterialsDao(connection, TRANSCRIPT_DAO, TASK_DAO, PROTOCOL_DAO);

    public static void copyDB() {
        Path sourceFile = Paths.get(DEFAULT_DB_PATH);
        Path destinationFile = Path.of(PlatformDependent.getPathToSaves()).resolve(DB_NAME);

        try {
            if (!Files.exists(destinationFile)) {
                Files.copy(sourceFile, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            System.err.println("Ошибка при копировании файла: " + e.getMessage());
        }
    }

    public static void initConnection() throws Exception {
        copyDB();
        String url = "jdbc:sqlite:" + PlatformDependent.getPathToSaves() + DB_NAME;
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

    public static TaskDao getTaskDao() {
        return TASK_DAO;
    }

    public static TagDao getTagDao() {
        return TAG_DAO;
    }

    public static ProtocolDao getProtocolDao() {
        return PROTOCOL_DAO;
    }

    public static MeetingMaterialsDao getMeetingMaterialsDao() {
        return MEETING_MATERIALS_DAO;
    }
}