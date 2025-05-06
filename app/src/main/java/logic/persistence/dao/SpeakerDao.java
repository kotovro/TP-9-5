package logic.persistence.dao;

import javafx.scene.image.Image;
import logic.general.Speaker;
import logic.persistence.exception.SpeakerNotFoundException;
import logic.utils.ImageSerializer;

import java.io.ByteArrayInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SpeakerDao {
    private final Connection connection;

    public SpeakerDao(Connection connection) {
        this.connection = connection;
    }

    public void addSpeaker(Speaker speaker) {
        try {
            String sql = "INSERT INTO speaker (name, image) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, speaker.getName());
                stmt.setBytes(2, ImageSerializer.imageToByteArray(speaker.getImage()));
                stmt.executeUpdate();
            }

            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid();")) {
                if (rs.next()) {
                    speaker.setId(rs.getInt(1));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteSpeaker(int speakerId) throws SQLException {
        String sql = "DELETE FROM speaker WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, speakerId);
            stmt.executeUpdate();
        }
    }

    public Speaker getSpeakerById(int speakerId) throws SQLException {
        String sql = "SELECT id, name, image FROM speaker WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, speakerId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    byte[] imageBytes = rs.getBytes("image");
                    Image image = imageBytes != null ? new Image(new ByteArrayInputStream(imageBytes)) : null;

                    return new Speaker(
                            rs.getString("name"),
                            image,
                            rs.getInt("id"));
                } else {
                    throw new SpeakerNotFoundException(speakerId);
                }
            }
        }
    }

    public List<Speaker> getAllSpeakers() {
        try {

            String sql = "SELECT id, name, image FROM speaker";
            List<Speaker> speakers = new ArrayList<>();

            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    byte[] imageBytes = rs.getBytes("image");
                    Image image = imageBytes != null ? new Image(new ByteArrayInputStream(imageBytes)) : null;
                    Speaker speaker = new Speaker(
                            rs.getString("name"),
                            image,
                            rs.getInt("id"));
                    speakers.add(speaker);
                }
            }
            return speakers;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
