package logic.persistence.dao;

import logic.general.Tag;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TagDao {
    private final Connection connection;

    public TagDao(Connection connection) {
        this.connection = connection;
    }

    public void addTag(Tag tag) {
        String sql = "INSERT INTO tag (name) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tag.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTag(Tag tag) {
        String sql = "DELETE FROM tag WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, tag.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
