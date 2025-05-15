package logic.persistence.dao;

import javafx.scene.image.Image;
import logic.general.Protocol;
import logic.general.Speaker;
import logic.general.Transcript;
import logic.persistence.exception.SpeakerNotFoundException;

import java.io.ByteArrayInputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ProtocolDao {
    private final Connection connection;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    public ProtocolDao(Connection connection) {
        this.connection = connection;
    }

    public void addProtocol(Protocol protocol) {
        try {
            String sql = "INSERT INTO protocol (conclusion, transcript_id) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, protocol.getText());
                stmt.setInt(2, protocol.getTranscriptId());
                stmt.executeUpdate();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
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


    public void deleteProtocol() throws SQLException {
//        String sql = "DELETE FROM speaker WHERE id = ?";
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setInt(1, protocolId);
//            stmt.executeUpdate();
//        }
    }

//    public Speaker getProtocolById(int protocolId) throws SQLException {
//        String sql = "SELECT id, name, image FROM speaker WHERE id = ?";
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setInt(1, speakerId);
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    byte[] imageBytes = rs.getBytes("image");
//                    Image image = imageBytes != null ? new Image(new ByteArrayInputStream(imageBytes)) : null;
//
//                    return new Speaker(
//                            rs.getString("name"),
//                            image,
//                            rs.getInt("id"));
//                } else {
//                    throw new SpeakerNotFoundException(speakerId);
//                }
//            }
//        }
//    }

    public List<Protocol> getAllProtocols() {
        try {

            String sql = "SELECT t.id AS transcript_id,  t.name, p.conclusion " +
                    "FROM transcript t " +
                    "JOIN protocol p ON t.id = p.transcript_id ";
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
