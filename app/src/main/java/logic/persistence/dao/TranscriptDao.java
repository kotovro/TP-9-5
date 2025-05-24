package logic.persistence.dao;

import logic.general.Replica;
import logic.general.Speaker;
import logic.general.Transcript;
import logic.general.Tag;
import logic.persistence.exception.EmptyTranscriptSaveAttemptException;
import logic.persistence.exception.UniqueTranscriptNameViolationException;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class TranscriptDao {
    private final Connection connection;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");


    public TranscriptDao(Connection connection) {
        this.connection = connection;
    }

    private void insertReplicas(int transcriptId, List<Replica> replicas) {
        try {
            if (replicas.isEmpty()) {
                return;
            }

            StringBuilder insertSql = new StringBuilder();
            insertSql.append("INSERT INTO replica (transcript_id, order_number, speaker_id, content) VALUES ");

            for (int i = 0; i < replicas.size(); i++) {
                insertSql.append("(?, ?, ?, ?)");
                if (i < replicas.size() - 1) {
                    insertSql.append(", ");
                }
            }

            try (PreparedStatement insertStmt = connection.prepareStatement(insertSql.toString())) {
                int paramIndex = 1;
                for (int i = 0; i < replicas.size(); i++) {
                    Replica replica = replicas.get(i);
                    insertStmt.setInt(paramIndex++, transcriptId);
                    insertStmt.setInt(paramIndex++, i + 1);
                    insertStmt.setInt(paramIndex++, replica.getSpeaker().getId());
                    insertStmt.setString(paramIndex++, replica.getText());
                }
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteTranscriptTags(int transcriptId) {
        String sql = "DELETE FROM transcript_tag WHERE transcript_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, transcriptId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertTranscriptTags(int transcriptId, List<Tag> tags) {
        if (tags.isEmpty()) {
            return;
        }

        String sql = "INSERT INTO transcript_tag (transcript_id, tag_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (Tag tag : tags) {
                stmt.setInt(1, transcriptId);
                stmt.setInt(2, tag.getId());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setTranscriptTags(Transcript transcript) {
        String fetchTagsSql = "SELECT t.id, t.name FROM tag t " +
                            "JOIN transcript_tag tt ON t.id = tt.tag_id " +
                            "WHERE tt.transcript_id = ?";
        List<Tag> tags = new ArrayList<>();
        try (PreparedStatement tagStmt = connection.prepareStatement(fetchTagsSql)) {
            tagStmt.setInt(1, transcript.getId());
            try (ResultSet tagRs = tagStmt.executeQuery()) {
                while (tagRs.next()) {
                    Tag tag = new Tag(tagRs.getString("name"));
                    tag.setId(tagRs.getInt("id"));
                    tags.add(tag);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        transcript.setTags(tags);
    }

    private boolean isTranscriptEmpty(Transcript transcript) {
        return transcript.getReplicas().isEmpty();
    }

    public void updateTranscript(Transcript transcript) {
        try {
            int transcriptId = transcript.getId();
            if (transcriptId == -1) {
                return;
            }

            if (isTranscriptEmpty(transcript))
            {
                throw new EmptyTranscriptSaveAttemptException("Нельзя сохранить пустую стенограмму!");
            }

            String checkNameSql = "SELECT name FROM transcript WHERE id = ?";
            String currentName = null;
            try (PreparedStatement checkStmt = connection.prepareStatement(checkNameSql)) {
                checkStmt.setInt(1, transcriptId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        currentName = rs.getString("name");
                    }
                }
            }

            if (!transcript.getName().equals(currentName)) {
                String updateTranscriptSql = "UPDATE transcript SET name = ?, date = ? WHERE id = ?";
                try (PreparedStatement updateStmt = connection.prepareStatement(updateTranscriptSql)) {
                    updateStmt.setString(1, transcript.getName());
                    updateStmt.setString(2, sdf.format(transcript.getDate()));
                    updateStmt.setInt(3, transcriptId);
                    updateStmt.executeUpdate();
                }
            } else {
                String updateDateSql = "UPDATE transcript SET date = ? WHERE id = ?";
                try (PreparedStatement updateStmt = connection.prepareStatement(updateDateSql)) {
                    updateStmt.setString(1, sdf.format(transcript.getDate()));
                    updateStmt.setInt(2, transcriptId);
                    updateStmt.executeUpdate();
                }
            }

            deleteReplicas(transcriptId);
            insertReplicas(transcriptId, transcript.getReplicas());

            List<Tag> tags = (List<Tag>) transcript.getTags();
            deleteTranscriptTags(transcriptId);
            insertTranscriptTags(transcriptId, tags);

        } catch (SQLException e) {
            if (e.getErrorCode() == 19) {
                throw new UniqueTranscriptNameViolationException(transcript.getName());
            } else {
                e.printStackTrace();
            }
        }
    }



    private void deleteReplicas(int transcriptId) throws SQLException {
        String deleteSql = "DELETE FROM replica WHERE transcript_id = ?";
        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSql)) {
            deleteStmt.setInt(1, transcriptId);
            deleteStmt.executeUpdate();
        }
    }

    public void deleteTranscript(Transcript transcript) {
        try {
            String deleteTranscript = "DELETE FROM transcript WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteTranscript)) {
                stmt.setInt(1, transcript.getId());
                stmt.executeUpdate();
            }
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public Transcript getTranscriptById(int transcriptId) {
        Transcript transcript = null;

        String sql = "SELECT t.id AS transcript_id, t.name, t.date, r.order_number, r.speaker_id, r.content " +
                "FROM transcript t " +
                "JOIN replica r ON t.id = r.transcript_id " +
                "WHERE t.id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, transcriptId);
            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    if (transcript == null) {
                        String rawDate = rs.getString("date");
                        Date date = sdf.parse(rawDate);
                        transcript = new Transcript(rs.getString("name"), date);
                        transcript.setId(transcriptId);
                        transcript.setReplicas(new ArrayList<>());
                        setTranscriptTags(transcript);
                    }
                    Replica replica = new Replica();
                    replica.setSpeaker(new Speaker(null, null, rs.getInt("speaker_id")));
                    replica.setText(rs.getString("content"));
                    transcript.addReplica(replica);
                }
                return transcript;
            }
        } catch (ParseException | SQLException e) {
            e.printStackTrace();
        }
        return transcript;
    }


    public void addTranscript(Transcript transcript) {
        try {
            if (isTranscriptEmpty(transcript))
            {
                throw new EmptyTranscriptSaveAttemptException("Нельзя сохранить пусутю стенограмму!");
            }
            String insertTranscriptSql = "INSERT INTO transcript (name, date) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertTranscriptSql)) {
                stmt.setString(1, transcript.getName());
                stmt.setString(2, sdf.format(transcript.getDate()));
                stmt.executeUpdate();
            }

            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid();")) {
                if (rs.next()) {
                    transcript.setId(rs.getInt(1));
                }
            }

            insertReplicas(transcript.getId(), transcript.getReplicas());

            List<Tag> tags = (List<Tag>) transcript.getTags();
            insertTranscriptTags(transcript.getId(), tags);

        } catch (SQLException e) {
            if (e.getErrorCode() == 19) {
                throw new UniqueTranscriptNameViolationException(transcript.getName());
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public List<Transcript> getTranscripts() {
        List<Transcript> transcripts;
        String sql = "SELECT t.id AS transcript_id, t.name, t.date, r.order_number, r.speaker_id, r.content " +
                "FROM transcript t " +
                "JOIN replica r ON t.id = r.transcript_id " +
                "ORDER BY t.id";

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
                    transcript.setId(transcriptId);
                    transcript.setReplicas(new ArrayList<>());
                    setTranscriptTags(transcript);
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

    public Optional<Transcript> getTranscriptByName(String name) {
        String sql = "SELECT t.id AS transcript_id, t.name, t.date, r.order_number, r.speaker_id, r.content " +
                "FROM transcript t " +
                "JOIN replica r ON t.id = r.transcript_id " +
                "WHERE t.name = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                Transcript transcript = null;

                while (rs.next()) {
                    if (transcript == null) {
                        String rawDate = rs.getString("date");
                        Date date = sdf.parse(rawDate);
                        transcript = new Transcript(rs.getString("name"), date);
                        transcript.setId(rs.getInt("transcript_id"));
                        transcript.setReplicas(new ArrayList<>());
                        setTranscriptTags(transcript);
                    }
                    Replica replica = new Replica();
                    replica.setSpeaker(new Speaker(null, null, rs.getInt("speaker_id")));
                    replica.setText(rs.getString("content"));
                    transcript.addReplica(replica);
                }
                return Optional.ofNullable(transcript);
            }
        } catch (ParseException | SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

}
