package logic.persistence;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.stream.Stream;

public class InitDatabase {

    private static final String SCHEMA_FILE = "logic/persistence/db_scheme/dbcreation.sql";
    private static final String DEFAULT_DB_PATH = "db_examples/test.db";

    @SuppressWarnings({"SqlNoDataSourceInspection", "SqlResolve", "SqlSourceToSinkFlow"})
    public static void main(String[] args) {
        args = new String[1];
        args[0] = SCHEMA_FILE;
        if (args.length == 0) {
            System.err.println("Не передан путь до файла базы данных.");
            System.exit(1);
        }

        Path dbFile = Paths.get(DEFAULT_DB_PATH);

        try {

            if (Files.exists(dbFile)) {
                System.out.println("Database already exists at: " + dbFile.toAbsolutePath());
                return;
            }

            Path parentDir = dbFile.getParent();
            if (parentDir != null && Files.notExists(parentDir)) {
                Files.createDirectories(parentDir);
                System.out.println("Directory created: " + parentDir.toAbsolutePath());
            }

            String url = "jdbc:sqlite:" + dbFile.toAbsolutePath();
            try (Connection conn = DriverManager.getConnection(url)) {
                if (conn != null) {
                    System.out.println("Connection to database established.");
                    try (Statement stmt = conn.createStatement()) {
                        stmt.execute("PRAGMA foreign_keys = ON;");
                        System.out.println("Foreign keys enabled.");
                    } catch (Exception e) {
                        System.err.println("Error enabling foreign keys: " + e.getMessage());
                    }

                    Path schemaPath = Paths.get(SCHEMA_FILE);
                    StringBuilder sb = new StringBuilder();
                    try (Stream<String> lines = Files.lines(schemaPath)) {
                        lines.forEach(line -> sb.append(line).append("\n"));
                    }

                    String[] statements = sb.toString().split(";");
                    for (String sql : statements) {
                        sql = sql.trim();
                        if (!sql.isEmpty()) {
                            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                                pstmt.execute();
                                System.out.println("Executed SQL: " + sql);
                            } catch (Exception e) {
                                System.err.println("Error executing SQL: " + sql);
                            }
                        }
                    }
                    System.out.println("Database initialized successfully.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
            System.exit(2);
        }
    }
}
