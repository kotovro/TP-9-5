package logic.persistence;

import logic.general.Speaker;
import logic.persistence.dao.SpeakerDao;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static ui.EditController.getImage;

public class DBInitializer {
    private static final String DB_CREATION_SCRIPT_PATH = "develop_resources/dbcreation.sql";
    private static final String DB_PATH = "dynamic-resources/saves/saves.db";

    public static void reinitDB() {
        deleteIfExist();
        try {
            createDB();
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteIfExist() {
        try {
            Files.deleteIfExists(Paths.get(DB_PATH).toAbsolutePath());
        } catch (IOException | NullPointerException e) {
            System.err.println("Failed to delete DB: " + e.getMessage());
        }
    }

    public static void createDB() throws IOException, SQLException {
        String sqlScript;
        try (Stream<String> lines = Files.lines(Paths.get(DB_CREATION_SCRIPT_PATH))) {
            sqlScript = lines.collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new IOException("Failed to read DB creation script: " + DB_CREATION_SCRIPT_PATH, e);
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
             Statement stmt = conn.createStatement()) {

            String[] commands = sqlScript.split(";");
            for (String command : commands) {
                if (!command.trim().isEmpty()) {
                    stmt.executeUpdate(command);
                }
            }

            SpeakerDao speakerDao = new SpeakerDao(conn);
            if (speakerDao.getAllSpeakers().isEmpty())
                addSpeakers(speakerDao);
        } catch (SQLException e) {
            throw new SQLException("Failed to initialize database", e);
        }
    }

    private static void addSpeakers(SpeakerDao speakerDao) {
        List<Speaker> speakers = List.of(
                new Speaker("Не выбран", getImage("/images/default_users/undefined.png"), 0),
                new Speaker("Соня", getImage("/images/default_users/sonya.png"), 1),
                new Speaker("Никита", getImage("/images/default_users/nikita.jpg"), 2),
                new Speaker("Виталий", getImage("/images/default_users/vitaly.png"), 3),
                new Speaker("Константин", getImage("/images/default_users/konstantin.jpg"), 4),
                new Speaker("Никита", getImage("/images/default_users/nikita.png"), 5),
                new Speaker("Полина", getImage("/images/default_users/polina.png"), 6)
        );
        for (Speaker speaker : speakers) {
            speakerDao.addSpeaker(speaker);
        }
    }

    public static void main(String[] args) {
        reinitDB();
    }
}
