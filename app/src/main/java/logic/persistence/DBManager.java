package logic.persistence;

import logic.general.Speaker;
import logic.persistence.dao.SpeakerDao;
import logic.persistence.dao.TranscriptDao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Stream;

import static ui.EditController.getImage;

public class DBManager {
    private static final String SCHEMA_FILE = "src/main/resources/db_scheme/dbcreation.sql";
    private static final String DEFAULT_DB_PATH = "db_examples/test.db";
    private static Connection connection;
    static {
        try {
            if (!Files.exists(Paths.get(DEFAULT_DB_PATH).toAbsolutePath())) {
                createDB();
            } else {
                initConnection();
            }
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
    private static final TranscriptDao TRANSCRIPT_DAO = new TranscriptDao(connection);
    private static final SpeakerDao SPEAKER_DAO = new SpeakerDao(connection);
    static {
        if (SPEAKER_DAO.getAllSpeakers().isEmpty()) addSpeakers();
    }

    public static void initConnection() throws Exception {
        Path dbFile = Paths.get(DEFAULT_DB_PATH).toAbsolutePath();
        String url = "jdbc:sqlite:" + dbFile;

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

    public static void createDB() throws SQLException, IOException {
        Path dbFile = Paths.get(DEFAULT_DB_PATH).toAbsolutePath();
        String url = "jdbc:sqlite:" + dbFile.toAbsolutePath();
        connection = DriverManager.getConnection(url);
        connection.setAutoCommit(true);
        Path schemaPath = Paths.get(SCHEMA_FILE);

        StringBuilder sb = new StringBuilder();
        try (Stream<String> lines = Files.lines(schemaPath)) {
            lines.forEach(line -> sb.append(line).append("\n"));
        }
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");

            String[] sqlCommands = sb.toString().split(";");
            for (String command : sqlCommands) {
                if (!command.trim().isEmpty()) {
                    stmt.execute(command);
                }
            }
        }
    }

    private static void addSpeakers() {
        List<Speaker> speakers = List.of(
                new Speaker("Не выбран", getImage("/images/UserSpeak2.png"), 0),
                new Speaker("Anna", getImage("/images/logo.png"), 1),
                new Speaker("Viktor", getImage("/images/UserSpeak2.png"), 2),
                new Speaker("Galina", getImage("/images/DangerCircle.png"), 3)
        );
        for (Speaker speaker : speakers) {
            getSpeakerDao().addSpeaker(speaker);
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static TranscriptDao getTranscriptDao() {
        return TRANSCRIPT_DAO;
    }

    public static SpeakerDao getSpeakerDao() {
        return SPEAKER_DAO;
    }
}
