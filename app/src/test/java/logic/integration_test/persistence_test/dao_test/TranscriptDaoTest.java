package logic.integration_test.persistence_test.dao_test;

import logic.general.Replica;
import logic.general.Speaker;
import logic.general.Tag;
import logic.general.Transcript;
import logic.persistence.DBInitializer;
import logic.persistence.dao.SpeakerDao;
import logic.persistence.dao.TagDao;
import logic.persistence.dao.TranscriptDao;
import logic.persistence.exception.UniqueTranscriptNameViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;
import static ui.EditController.getImage;

public class TranscriptDaoTest {
    private static final String DB_PATH = "dynamic-resources/saves/saves.db";
    private Connection connection;
    private TranscriptDao transcriptDao;
    private SpeakerDao speakerDao;
    private TagDao tagDao;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private Speaker testSpeaker;
    private Tag testTag;

    @BeforeEach
    void setUp() throws IOException, SQLException {
        DBInitializer.deleteIfExist();
        DBInitializer.reinitDB();

        connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
        transcriptDao = new TranscriptDao(connection);
        speakerDao = new SpeakerDao(connection);
        tagDao = new TagDao(connection);

        testTag = new Tag("Test Tag");
        tagDao.addTag(testTag);

        testSpeaker = new Speaker("Соня", getImage("/images/default_users/undefined.png"), 0);
        speakerDao.addSpeaker(testSpeaker);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        DBInitializer.deleteIfExist();
    }

    @Test
    void testAddTranscript() throws Exception {
        Transcript transcript = new Transcript("Test Transcript", new Date());
        List<Replica> replicas = new ArrayList<>();
        replicas.add(new Replica("Test content", testSpeaker));
        transcript.setReplicas(replicas);
        List<Tag> tags = new ArrayList<>();
        tags.add(testTag);
        transcript.setTags(tags);

        transcriptDao.addTranscript(transcript);

        assertTrue(transcript.getId() > 0, "Transcript ID should be set");
        Transcript retrieved = transcriptDao.getTranscriptById(transcript.getId());
        assertEquals("Test Transcript", retrieved.getName(), "Transcript name should match");
        List<Replica> retrievedReplicas = StreamSupport.stream(retrieved.getReplicas().spliterator(), false)
                .collect(Collectors.toList());
        assertEquals(1, retrievedReplicas.size(), "One replica should be added");
        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery("SELECT tag_id FROM transcript_tag WHERE transcript_id = " + transcript.getId())) {
            assertTrue(rs.next(), "Tag should be associated");
            assertEquals(testTag.getId(), rs.getInt("tag_id"));
        }
    }

    @Test
    void testAddTranscriptUniqueNameViolation() throws Exception {
        Transcript transcript1 = new Transcript("Test Transcript", new Date());
        transcriptDao.addTranscript(transcript1);

        Transcript transcript2 = new Transcript("Test Transcript", new Date());
        assertThrows(UniqueTranscriptNameViolationException.class,
                () -> transcriptDao.addTranscript(transcript2),
                "Should throw UniqueTranscriptNameViolationException");
    }

    @Test
    void testUpdateTranscript() throws Exception {
        Transcript transcript = new Transcript("Test Transcript", new Date());
        List<Replica> replicas = new ArrayList<>();
        replicas.add(new Replica("Initial content", testSpeaker));
        transcript.setReplicas(replicas);
        transcriptDao.addTranscript(transcript);

        transcript.setName("Updated Transcript");
        replicas.clear();
        replicas.add(new Replica("Updated content", testSpeaker));
        List<Tag> tags = new ArrayList<>();
        tags.add(testTag);
        transcript.setTags(tags);
        transcriptDao.updateTranscript(transcript);

        Transcript updated = transcriptDao.getTranscriptById(transcript.getId());
        assertEquals("Updated Transcript", updated.getName(), "Transcript name should be updated");
        List<Replica> updatedReplicas = StreamSupport.stream(updated.getReplicas().spliterator(), false)
                .collect(Collectors.toList());
        assertEquals("Updated content", updatedReplicas.get(0).getText(), "Replica content should be updated");
        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery("SELECT tag_id FROM transcript_tag WHERE transcript_id = " + transcript.getId())) {
            assertTrue(rs.next(), "Tag should be associated");
            assertEquals(testTag.getId(), rs.getInt("tag_id"));
        }
    }

    @Test
    void testDeleteTranscript() throws Exception {
        Transcript transcript = new Transcript("Test Transcript", new Date());
        transcriptDao.addTranscript(transcript);

        transcriptDao.deleteTranscript(transcript);
        assertNull(transcriptDao.getTranscriptById(transcript.getId()), "Transcript should be deleted");
    }

    @Test
    void testGetTranscriptById() throws Exception {
        Transcript transcript = new Transcript("Test Transcript", new Date());
        List<Replica> replicas = new ArrayList<>();
        replicas.add(new Replica("Test content", testSpeaker));
        transcript.setReplicas(replicas);
        transcriptDao.addTranscript(transcript);

        Transcript retrieved = transcriptDao.getTranscriptById(transcript.getId());
        assertEquals("Test Transcript", retrieved.getName(), "Transcript name should match");
        List<Replica> retrievedReplicas = StreamSupport.stream(retrieved.getReplicas().spliterator(), false)
                .collect(Collectors.toList());
        assertEquals(1, retrievedReplicas.size(), "One replica should be retrieved");
        assertEquals("Test content", retrievedReplicas.get(0).getText(), "Replica content should match");
    }

    @Test
    void testGetTranscripts() throws Exception {
        Transcript transcript1 = new Transcript("Transcript 1", new Date());
        Transcript transcript2 = new Transcript("Transcript 2", new Date());
        List<Replica> replicas = new ArrayList<>();
        replicas.add(new Replica("Test content", testSpeaker));
        transcript1.setReplicas(replicas);
        transcript2.setReplicas(replicas);
        transcriptDao.addTranscript(transcript1);
        transcriptDao.addTranscript(transcript2);

        var transcripts = transcriptDao.getTranscripts();
        assertEquals(2, transcripts.size(), "Two transcripts should be retrieved");
        assertTrue(transcripts.stream().anyMatch(t -> "Transcript 1".equals(t.getName())), "Transcript 1 should exist");
        assertTrue(transcripts.stream().anyMatch(t -> "Transcript 2".equals(t.getName())), "Transcript 2 should exist");
    }

    @Test
    void testGetTranscriptByName() throws Exception {
        Transcript transcript = new Transcript("Test Transcript", new Date());
        List<Replica> replicas = new ArrayList<>();
        replicas.add(new Replica("Test content", testSpeaker));
        transcript.setReplicas(replicas);
        transcriptDao.addTranscript(transcript);

        var optionalTranscript = transcriptDao.getTranscriptByName("Test Transcript");
        assertTrue(optionalTranscript.isPresent(), "Transcript should be found");
        assertEquals("Test Transcript", optionalTranscript.get().getName(), "Transcript name should match");

        var notFound = transcriptDao.getTranscriptByName("Non-existent");
        assertFalse(notFound.isPresent(), "Non-existent transcript should not be found");
    }

//    @Test
//    void testUpdateTranscriptWithTags() throws Exception {
//        Transcript transcript = new Transcript("Test Transcript", new Date());
//        transcriptDao.addTranscript(transcript);
//
//        Tag newTag = new Tag("New Test Tag");
//        tagDao.addTag(newTag);
//        List<Tag> tags = new ArrayList<>();
//        tags.add(testTag);
//        tags.add(newTag);
//        transcript.setTags(tags);
//
//        transcriptDao.updateTranscript(transcript);
//
//        Transcript updated = transcriptDao.getTranscriptById(transcript.getId());
//        assertNotNull(updated, "Updated transcript should not be null");
//        List<Tag> updatedTags = StreamSupport.stream(updated.getTags().spliterator(), false)
//                .collect(Collectors.toList());
//        assertEquals(2, updatedTags.size(), "Transcript should have two tags");
//        assertTrue(updatedTags.contains(testTag), "Should contain first tag");
//        assertTrue(updatedTags.contains(newTag), "Should contain second tag");
//    }
//
//    @Test
//    void testUpdateTranscriptRemovingAllReplicas() throws Exception {
//        Transcript transcript = new Transcript("Test Transcript", new Date());
//        List<Replica> replicas = new ArrayList<>();
//        replicas.add(new Replica("Initial content", testSpeaker));
//        transcript.setReplicas(replicas);
//        transcriptDao.addTranscript(transcript);
//
//        transcript.setReplicas(new ArrayList<>());
//        transcriptDao.updateTranscript(transcript);
//
//        Transcript updated = transcriptDao.getTranscriptById(transcript.getId());
//        assertTrue(StreamSupport.stream(updated.getReplicas().spliterator(), false)
//                .collect(Collectors.toList()).isEmpty(), "Transcript should have no replicas");
//    }
//
//    @Test
//    void testGetTranscriptsWithFiltering() throws Exception {
//        Date now = new Date();
//        Transcript transcript1 = new Transcript("AAA Transcript", now);
//        Transcript transcript2 = new Transcript("BBB Transcript", now);
//        Transcript transcript3 = new Transcript("CCC Transcript", now);
//
//        transcriptDao.addTranscript(transcript1);
//        transcriptDao.addTranscript(transcript2);
//        transcriptDao.addTranscript(transcript3);
//
//        List<Transcript> transcripts = transcriptDao.getTranscripts();
//        List<Transcript> filteredTranscripts = transcripts.stream()
//                .filter(t -> t.getName().contains("BBB"))
//                .collect(Collectors.toList());
//
//        assertEquals(1, filteredTranscripts.size(), "Should find one transcript");
//        assertEquals("BBB Transcript", filteredTranscripts.get(0).getName(), "Should find correct transcript");
//    }
}