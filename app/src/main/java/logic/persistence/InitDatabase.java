package logic.persistence;

import javafx.scene.image.Image;
import logic.general.Speaker;
import logic.persistence.dao.SpeakerDao;
import ui.EditController;

import java.sql.Connection;
import java.sql.SQLException;

public class InitDatabase {
    public static void main(String[] args) throws SQLException {
        Connection connection = null;
        try  {

            connection = DBManager.getConnection();
            Speaker speaker = new Speaker("TestSpeaker",
                    new Image(EditController.class.getResourceAsStream("/images/logo.png")), 1);
            SpeakerDao speakerDao = new SpeakerDao(connection);
            speakerDao.addSpeaker(speaker);
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }




        //        Transcript transcript = new Transcript("Test", new Date());

//        transcript.addReplica(new Replica("Test replica", new Speaker("TestSpeaker",
//                new Image(EditController.class.getResourceAsStream("/images/logo.png")), 1)));
//        TranscriptDao transcriptDao = new TranscriptDao(connection);
//        try {
//            TranscriptDao transcriptDao = new TranscriptDao(connection);
//            List<Transcript> transcriptList = transcriptDao.getTranscripts();
//            for (Transcript transcript : transcriptList) {
//                System.out.println(transcript.getDate());
//            }
//            connection.commit();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

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
//            ParticipantDao participantDao = new ParticipantDao(connection);
//
//            Participant newParticipant = new Participant();
//            newParticipant.setName("Test User");
//            participantDao.addParticipant(newParticipant);
//            System.out.println("Added participant with ID: " + newParticipant.getId());
//
//            Participant fetched = participantDao.getParticipantById(newParticipant.getId());
//            System.out.println("Fetched participant: " + fetched.getName());
//
//            System.out.println("All participants:");
//            for (Participant p : participantDao.getAllParticipants()) {
//                System.out.println(p.getId() + ": " + p.getName());
//            }
//
//            participantDao.deleteParticipant(newParticipant.getId());
//            System.out.println("Deleted participant with ID: " + newParticipant.getId());
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