package logic.integration_test.persistence_test.dao_test;

import logic.general.Tag;
import logic.persistence.DBInitializer;
import logic.persistence.dao.TagDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class TagDaoTest {
    private static final String DB_PATH = "dynamic-resources/saves/saves.db";
    private Connection connection;
    private TagDao tagDao;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
        tagDao = new TagDao(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    void testAddTag() throws SQLException {
        Tag tag = new Tag("Test Tag");
        tagDao.addTag(tag);

        assertTrue(tag.getId() > 0, "Tag ID should be set after insertion");
        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery("SELECT name FROM tag WHERE id = " + tag.getId())) {
            assertTrue(rs.next(), "Tag should exist");
            assertEquals("Test Tag", rs.getString("name"));
        }
    }

    @Test
    void testDeleteTag() throws SQLException {
        Tag tag = new Tag("Test Tag");
        tagDao.addTag(tag);

        tagDao.deleteTag(tag);
        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM tag WHERE id = " + tag.getId())) {
            assertTrue(rs.next());
            assertEquals(0, rs.getInt("count"), "Tag should be deleted");
        }
    }
}
