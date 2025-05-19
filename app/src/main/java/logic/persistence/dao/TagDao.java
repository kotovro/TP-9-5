package logic.persistence.dao;

import logic.general.Tag;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

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

            try (Statement stmt2 = connection.createStatement();
                 ResultSet rs = stmt2.executeQuery("SELECT last_insert_rowid();")) {
                if (rs.next()) {
                    tag.setId(rs.getInt(1));
                }
            }
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
