package logic.db_tests;

import logic.persistence.DBManager;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class InitDatabaseTest {
    private static Connection connection;

    @BeforeAll
    static void initDatabase() throws Exception {
        connection = DBManager.getConnection();
    }

    @AfterAll
    static void closeResources() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    @DisplayName("Проверка создания таблиц")
    void testTablesCreation() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            assertDoesNotThrow(() -> stmt.executeQuery("SELECT 1 FROM speaker LIMIT 1"),
                    "Таблица speaker должна существовать");

            assertDoesNotThrow(() -> stmt.executeQuery("SELECT 1 FROM tag LIMIT 1"),
                    "Таблица tag должна существовать");

            assertDoesNotThrow(() -> stmt.executeQuery("SELECT 1 FROM meeting LIMIT 1"),
                    "Таблица meeting должна существовать");

            assertDoesNotThrow(() -> stmt.executeQuery("SELECT 1 FROM transcript LIMIT 1"),
                    "Таблица transcript должна существовать");

            assertDoesNotThrow(() -> stmt.executeQuery("SELECT 1 FROM replica LIMIT 1"),
                    "Таблица replica должна существовать");

            assertDoesNotThrow(() -> stmt.executeQuery("SELECT 1 FROM protocol LIMIT 1"),
                    "Таблица protocol должна существовать");

            assertDoesNotThrow(() -> stmt.executeQuery("SELECT 1 FROM task LIMIT 1"),
                    "Таблица task должна существовать");

            assertDoesNotThrow(() -> stmt.executeQuery("SELECT 1 FROM speaker_tag LIMIT 1"),
                    "Таблица speaker_tag должна существовать");
        }
    }
}