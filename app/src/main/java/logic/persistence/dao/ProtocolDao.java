package logic.persistence.dao;

import javafx.scene.image.Image;
import logic.general.*;
import logic.persistence.exception.SpeakerNotFoundException;
import logic.persistence.exception.TranscriptDoesNotExistException;

import java.io.ByteArrayInputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class ProtocolDao {
    private final Connection connection;
    private final TaskDao taskDao;
    private final TranscriptDao transcriptDao;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    public ProtocolDao(Connection connection) {
        this.connection = connection;
        this.taskDao = new TaskDao(connection);
        this.transcriptDao = new TranscriptDao(connection);
    }

    public void addProtocol(Protocol protocol) {
        try {
            if (protocol.getTranscriptId() == -1)
            {
                throw new TranscriptDoesNotExistException("Transcript does not exist");
            }
            String sql = "INSERT INTO protocol (conclusion, transcript_id) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, protocol.getText());
                stmt.setInt(2, protocol.getTranscriptId());
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void updateProtocol(Protocol protocol) {
        try {
            String sql = "UPDATE protocol SET conclusion = ?  WHERE protocol_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, protocol.getText());
                stmt.setInt(2, protocol.getTranscriptId());
                stmt.executeUpdate();
            }
        } catch (
                Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<MeetingMaterials> getAllMeetingMaterials() {
        try {
            String sql = "SELECT t.*, p.conclusion as protocol_conclusion, " +
                        "tk.id as task_id, tk.description as task_description, tk.assignee_id as task_assignee_id, " +
                        "r.order_number, r.speaker_id, r.content, " +
                        "tg.id as tag_id, tg.name as tag_name " +
                        "FROM transcript t " +
                        "LEFT JOIN protocol p ON t.id = p.transcript_id " +
                        "LEFT JOIN task tk ON t.id = tk.transcript_id " +
                        "LEFT JOIN replica r ON t.id = r.transcript_id " +
                        "LEFT JOIN transcript_tag tt ON t.id = tt.transcript_id " +
                        "LEFT JOIN tag tg ON tt.tag_id = tg.id " +
                        "ORDER BY t.id, r.order_number, tk.id";
            
            Map<Integer, MeetingMaterials> materialsMap = new HashMap<>();
            
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                while (rs.next()) {
                    int transcriptId = rs.getInt("id");
                    MeetingMaterials materials = materialsMap.get(transcriptId);
                    
                    if (materials == null) {
                        Transcript transcript = new Transcript(
                            rs.getString("name"),
                            sdf.parse(rs.getString("date"))
                        );
                        transcript.setId(transcriptId);
                        transcript.setReplicas(new ArrayList<>());
                        transcript.setTags(new ArrayList<>());
                        
                        // Create protocol if exists
                        Optional<Protocol> protocol = rs.getString("protocol_conclusion") != null ?
                            Optional.of(new Protocol(transcriptId, rs.getString("protocol_conclusion"))) :
                            Optional.empty();
                            
                        materials = new MeetingMaterials(transcript, protocol, new ArrayList<>());
                        materialsMap.put(transcriptId, materials);
                    }

                    if (rs.getInt("order_number") != 0) {
                        Replica replica = new Replica();
                        replica.setSpeaker(new Speaker(null, null, rs.getInt("speaker_id")));
                        replica.setText(rs.getString("content"));
                        materials.getTranscript().addReplica(replica);
                    }

                    if (rs.getInt("task_id") != 0) {
                        Task task = new Task(transcriptId, rs.getString("task_description"));
                        task.setId(rs.getInt("task_id"));
                        task.setAssigneeId(rs.getInt("task_assignee_id"));
                        materials.getTasks().add(task);
                    }

                    if (rs.getInt("tag_id") != 0) {
                        Tag tag = new Tag(rs.getString("tag_name"));
                        tag.setId(rs.getInt("tag_id"));
                        List<Tag> tags = (List<Tag>) materials.getTranscript().getTags();
                        if (!tags.contains(tag)) {
                            tags.add(tag);
                        }
                    }
                }
            }

            return new ArrayList<>(materialsMap.values());
            
        } catch (SQLException | ParseException e) {
            throw new RuntimeException("Error retrieving meeting materials", e);
        }
    }

    public void deleteProtocol(Protocol protocol) {
        String sql = "DELETE FROM protocol WHERE transcript_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, protocol.getTranscriptId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Protocol> getAllProtocols() {
        try {
            String sql = "SELECT * FROM protocol";
            List<Protocol> protocols = new ArrayList<>();

            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    Protocol protocol = new Protocol(rs.getInt("transcript_id"), rs.getString("conclusion"));
                    protocols.add(protocol);
                }
            }
            return protocols;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
