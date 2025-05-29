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
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    public ProtocolDao(Connection connection) {
        this.connection = connection;
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
            String sql = "SELECT * FROM protocol ORDER BY transcript_id";
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
