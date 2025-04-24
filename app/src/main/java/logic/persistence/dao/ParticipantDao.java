package logic.persistence.dao;

import logic.general.Speaker;
import logic.persistence.exception.ParticipantNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParticipantDao {
    private final Connection connection;

    public ParticipantDao(Connection connection) {
        this.connection = connection;
    }

    public void addParticipant(Speaker speaker) throws SQLException {
        String sql = "INSERT INTO participant (name) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, speaker.getName());
            stmt.executeUpdate();
        }
    }

    public void deleteParticipant(Long participantId) throws SQLException {
        String sql = "DELETE FROM participant WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, participantId);
            stmt.executeUpdate();
        }
    }

    public Speaker getParticipantById(Long participantId) throws SQLException {
        String sql = "SELECT id, name FROM participant WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, participantId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Speaker(
                            rs.getString("name"),
                            null,
                            rs.getLong("id"));
                } else {
                    throw new ParticipantNotFoundException(participantId);                }
            }
        }
    }

    public List<Speaker> getAllParticipants() throws SQLException {
        String sql = "SELECT id, name FROM participant";
        List<Speaker> participants = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Speaker speaker = new Speaker(
                        rs.getString("name"),
                        null,
                        rs.getLong("id"));
                participants.add(speaker);
            }
        }

        return participants;
    }
}
