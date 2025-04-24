package logic.persistence.dao;

import logic.general.Participant;
import logic.persistence.exception.ParticipantNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParticipantDao {
    private final Connection connection;

    public ParticipantDao(Connection connection) {
        this.connection = connection;
    }

    public void addParticipant(Participant participant) throws SQLException {
        String sql = "INSERT INTO participant (name) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, participant.getName());
            stmt.executeUpdate();
        }

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid();")) {
            if (rs.next()) {
                participant.setId(rs.getLong(1));
            }
        }
    }


    public void deleteParticipant(Long participantId) throws SQLException {
        String sql = "DELETE FROM participant WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, participantId);
            stmt.executeUpdate();
        }
    }

    public Participant getParticipantById(Long participantId) throws SQLException {
        String sql = "SELECT id, name FROM participant WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, participantId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Participant participant = new Participant();
                    participant.setId(rs.getLong("id"));
                    participant.setName(rs.getString("name"));
                    return participant;
                } else {
                    throw new ParticipantNotFoundException(participantId);                }
            }
        }
    }

    public List<Participant> getAllParticipants() throws SQLException {
        String sql = "SELECT id, name FROM participant";
        List<Participant> participants = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Participant participant = new Participant();
                participant.setId(rs.getLong("id"));
                participant.setName(rs.getString("name"));
                participants.add(participant);
            }
        }

        return participants;
    }
}
