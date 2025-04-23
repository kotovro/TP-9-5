package logic.persistence;

import logic.general.Participant;
import logic.general.Replica;
import logic.persistence.dao.ParticipantDao;
import logic.persistence.dao.ReplicaDao;

import java.sql.Connection;
import java.sql.SQLException;

public class InitDatabase {
    public static void main(String[] args) throws SQLException {
 //       Connection connection = DBManager.getConnection();
        //ReplicaDao replicaDao = new ReplicaDao(connection);
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