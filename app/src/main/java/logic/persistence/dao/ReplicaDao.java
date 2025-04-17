package logic.persistence.dao;

import logic.general.Participant;
import logic.general.Replica;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReplicaDao {
    private final Connection connection;

    public ReplicaDao(Connection connection) {
        this.connection = connection;
    }

    private int getNextOrderNumber(int meetingId) throws SQLException {
        String sql = "SELECT COALESCE(MAX(order_number), 0) + 1 AS next_order "
                + "FROM replica WHERE meeting_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, meetingId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("next_order");
                } else {
                    return 1;
                }
            }
        }
    }

    public void addReplica(int meetingId, Replica replica) throws SQLException {
        int orderNumber = getNextOrderNumber(meetingId);

        String sql = "INSERT INTO replica (meeting_id, order_number, participant_id, content) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, meetingId);
            stmt.setInt(2, orderNumber);
            stmt.setLong(3, replica.getSpeaker().getId());
            stmt.setString(4, replica.getText());
            stmt.executeUpdate();
        }
    }

    public List<Replica> getReplicasByMeetingId(int meetingId) throws SQLException {
        String sql = "SELECT order_number, content FROM replica WHERE meeting_id = ? ORDER BY order_number";
        List<Replica> replicas = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, meetingId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Replica replica = new Replica();
                    Participant participant = new Participant();
                    participant.setId(rs.getLong("participant_id"));
                    replica.setSpeaker(participant);
                    replica.setText(rs.getString("content"));
                    replicas.add(replica);
                }
            }
        }

        return replicas;
    }
}
