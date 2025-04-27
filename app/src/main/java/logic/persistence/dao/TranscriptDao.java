package logic.persistence.dao;

import logic.general.Replica;
import logic.general.Speaker;
import logic.general.Transcript;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranscriptDao {
    private final Connection connection;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");


    public TranscriptDao(Connection connection) {
        this.connection = connection;
    }

    private int getNextOrderNumber(int meetingId) throws SQLException {
        String sql = "SELECT COALESCE(MAX(order_number), 0) + 1 AS next_order "
                + "FROM replica WHERE transcript_id = ?";
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

    public void deleteTranscript(Transcript transcript) {
        try {
            String getTranscriptIdSql = "SELECT id from transcript where name=?";
            int transcriptId = -1;
            try (PreparedStatement stmt = connection.prepareStatement(getTranscriptIdSql)) {
                stmt.setString(1, transcript.getName());
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        transcriptId = rs.getInt("id");
                    }
                }
            }

            if (transcriptId != -1) {
                String sql = "DELETE FROM replica WHERE transcript_id = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql))
                {
                    stmt.setInt(1, transcriptId);
                    stmt.executeUpdate();
                }
            }

            String deleteTranscript = "DELETE FROM transcript WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteTranscript)) {
                stmt.setInt(1, transcriptId);
                stmt.executeUpdate();
            }
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
    }


    public void addTranscript(Transcript transcript) {
        try {
            String insertTranscriptSql = "INSERT INTO transcript (name, date) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertTranscriptSql)) {
                stmt.setString(1, transcript.getName());
                stmt.setString(2, sdf.format(transcript.getDate()));
                stmt.executeUpdate();
            }

            int transcriptId = -1;
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid();")) {
                if (rs.next()) {
                    transcriptId = rs.getInt(1);
                }
            }

            Iterable<Replica> replicas = transcript.getReplicas();

            for (Replica replica : replicas) {
                String sql = "INSERT INTO replica (transcript_id, order_number, speaker_id, content) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, transcriptId);
                    stmt.setInt(2, getNextOrderNumber(transcriptId));
                    stmt.setInt(3, replica.getSpeaker().getId());
                    stmt.setString(4, replica.getText());
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Transcript> getTranscripts() {
        List<Transcript> transcripts;
        String sql = "SELECT t.id AS transcript_id, t.name, t.date, r.order_number, r.speaker_id, r.content " +
                "FROM transcript t " +
                "JOIN replica r ON t.id = r.transcript_id ";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            Map<Integer, Transcript> transcriptMap = new HashMap<>();

            while (rs.next()) {
                int transcriptId = rs.getInt("transcript_id");
                Transcript transcript = transcriptMap.get(transcriptId);
                if (transcript == null) {
                    String rawDate = rs.getString("date");
                    Date date = sdf.parse(rawDate);
                    transcript = new Transcript(rs.getString("name"), date);

                    transcript.setReplicas(new ArrayList<>());
                    transcriptMap.put(transcriptId, transcript);
                }
                Replica replica = new Replica();
                replica.setSpeaker(new Speaker(null, null, rs.getInt("speaker_id")));
                replica.setText(rs.getString("content"));
                transcript.addReplica(replica);
            }
            transcripts = new ArrayList<>(transcriptMap.values());
        } catch (ParseException | SQLException e) {
            throw new RuntimeException(e);
        }

        return transcripts;
    }

}
