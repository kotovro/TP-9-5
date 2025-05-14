package logic.persistence;

import javafx.scene.image.Image;
import logic.general.Protocol;
import logic.general.Replica;
import logic.general.Speaker;
import logic.general.Transcript;
import logic.persistence.dao.ProtocolDao;
import logic.persistence.dao.SpeakerDao;
import logic.persistence.dao.TranscriptDao;
import ui.EditController;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class InitDatabase {
    public static void main(String[] args) throws SQLException {
        Connection connection = null;
        try  {

            connection = DBManager.getConnection();
            Speaker speaker = new Speaker("TestSpeaker",
                    new Image(EditController.class.getResourceAsStream("/images/logo.png")), 1);
            SpeakerDao speakerDao = new SpeakerDao(connection);
            speakerDao.addSpeaker(speaker);


        } catch (Exception e) {
            e.printStackTrace();
        }




        Transcript transcript = new Transcript("Test", new Date());

        transcript.addReplica(new Replica("Test replica", new Speaker("TestSpeaker",
                new Image(EditController.class.getResourceAsStream("/images/logo.png")), 1)));

        TranscriptDao transcriptDao = new TranscriptDao(connection);
        transcriptDao.addTranscript(transcript);
        try {
            List<Transcript> transcriptList = transcriptDao.getTranscripts();
            for (Transcript t : transcriptList) {
                System.out.println(t.getDate());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Protocol protocol = new Protocol(transcript.getName(), "Silly text");
        ProtocolDao protocolDao = new ProtocolDao(connection);
        protocolDao.addProtocol(protocol);
        try {
            List<Protocol> protocolList = protocolDao.getAllProtocols();
            for (Protocol p : protocolList) {
                System.out.println(p.getText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Replica replica = new Replica();
//        Participant p = new Participant();
//        p.setName("Check");
//        p.setId(1L);
//        replica.setSpeaker(p);
//        replica.setText("Test");
//        replicaDao.addReplica(1, replica);
//        connection.commit();
//        Connection connection = null;
//
//        try {
//            connection = DBManager.getConnection();
//            connection.setAutoCommit(false);
//
//
//
//            connection.commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//            try {
//                if (connection != null) connection.rollback();
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }

    }
}