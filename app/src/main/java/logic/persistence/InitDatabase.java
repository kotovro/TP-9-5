package logic.persistence;

import logic.persistence.dao.ReplicaDao;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.stream.Stream;

public class InitDatabase {
    private static final String SCHEMA_FILE = "src/main/java/logic/persistence/db_scheme/dbcreation.sql";
    private static final String DEFAULT_DB_PATH = "../../../db_examples/test.db";
    private static Connection connection;

    public static Connection getConnection() throws Exception {
        if (connection == null || connection.isClosed()) {
            Path dbFile = Paths.get(DEFAULT_DB_PATH);
            String url = "jdbc:sqlite:" + dbFile.toAbsolutePath();
            connection = DriverManager.getConnection(url);
            connection.setAutoCommit(false);
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    @SuppressWarnings({"SqlNoDataSourceInspection", "SqlResolve", "SqlSourceToSinkFlow"})
    public static void main(String[] args) {
        try {
            Path dbFile = Paths.get(DEFAULT_DB_PATH);
            Path parentDir = dbFile.getParent();
            if (parentDir != null && Files.notExists(parentDir)) {
                Files.createDirectories(parentDir);
                System.out.println("Directory created: " + parentDir.toAbsolutePath());
            }

            Connection conn = getConnection();

            System.out.println("Connection to database established.");
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
                System.out.println("Foreign keys enabled.");
            }
            Path schemaPath = Paths.get(SCHEMA_FILE);
            System.out.println("Looking for schema file at: " + schemaPath.toAbsolutePath());
            if (!Files.exists(schemaPath)) {
                throw new FileNotFoundException("Schema file not found at: " + schemaPath.toAbsolutePath());
            }
            StringBuilder sb = new StringBuilder();
            try (Stream<String> lines = Files.lines(schemaPath)) {
                lines.forEach(line -> sb.append(line).append("\n"));
                try (Statement stmt = conn.createStatement()) {
                    // Drop tables if they exist (add all your tables here)
                    stmt.execute("DROP TABLE IF EXISTS participant;");
                    stmt.execute("DROP TABLE IF EXISTS tag;");
                    stmt.execute("DROP TABLE IF EXISTS meeting;");
                    stmt.execute("DROP TABLE IF EXISTS replica;");
                    stmt.execute("DROP TABLE IF EXISTS protocol;");
                    stmt.execute("DROP TABLE IF EXISTS task;");
                    stmt.execute("DROP TABLE IF EXISTS participant_tag;");

                    // add all your tables

                    // Then proceed with your schema creation
                    String[] sqlCommands = sb.toString().split(";");
                    for (String command : sqlCommands) {
                        if (!command.trim().isEmpty()) {
                            stmt.execute(command);
                        }
                    }
                    conn.commit();
                }
                try (Statement stmt = conn.createStatement()) {
                    String[] sqlCommands = sb.toString().split(";");
                    for (String command : sqlCommands) {
                        if (!command.trim().isEmpty()) {
                            stmt.execute(command);
                        }
                    }
                    conn.commit();
                    System.out.println("Database schema created successfully.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (Exception ex) {
                System.err.println("Error rolling back: " + ex.getMessage());
            }
        } finally {
            closeConnection();
        }
        ReplicaDao dao;
    }
}