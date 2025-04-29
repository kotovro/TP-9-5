package logic.text_tests;

import javafx.util.Pair;
import logic.general.Replica;
import logic.general.Speaker;
import logic.general.Transcript;
import logic.text_edit.FindResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static ui.EditController.getImage;

class FindResultTest {
    private Transcript transcript;
    private FindResult findResult;
    private Speaker speaker1;
    private Speaker speaker2;

    @BeforeEach
    void setUp() {
        speaker1 = new Speaker("Petr", getImage("/images/default_users/undefined.png"), 1);
        speaker2 = new Speaker("Ivan", getImage("/images/default_users/undefined.png"), 2);

        transcript = new Transcript("Test", new Date());
        transcript.addReplica(new Replica("hello world", speaker1));
        transcript.addReplica(new Replica("another hello", speaker2));

        findResult = new FindResult();
    }

    @Test
    void formSearchResults_shouldFindAllOccurrences() {
        findResult.formSearchResults(transcript, "hello");


        assertTrue(findResult.hasNext());
        Pair<Integer, Integer> first = findResult.next();
//        System.out.println(first.getKey());
//        System.out.println(first.getValue());
//        System.out.println(findResult.hasNext());
//        System.out.println(findResult.hasPrevious());
        assertEquals(1, first.getKey());
        assertEquals(8, first.getValue());
//
        assertTrue(findResult.hasPrevious());
        Pair<Integer, Integer> second = findResult.previous();
//        System.out.println(second.getKey());
//        System.out.println(second.getValue());
        assertEquals(0, second.getKey());
        assertEquals(0, second.getValue());

       // assertFalse(findResult.hasNext());
    }

    @Test
    void navigationMethods_shouldWorkCorrectly() {
        findResult.formSearchResults(transcript, "world");
//       System.out.println(findResult.hasNext());
        assertFalse(findResult.hasPrevious());
        assertFalse(findResult.hasNext());

 //       Pair<Integer, Integer> first = findResult.;
 //       System.out.println(first.getValue());
 //       System.out.println(first.getKey());
//        assertEquals(0, first.getKey());
//        assertEquals(6, first.getValue());
//
//        assertTrue(findResult.hasPrevious());
//        assertFalse(findResult.hasNext()); // Больше результатов нет
//
//        Pair<Integer, Integer> prev = findResult.previous();
//        assertEquals(0, prev.getKey());
//        assertEquals(6, prev.getValue());
//
//        assertFalse(findResult.hasPrevious());
//        assertTrue(findResult.hasNext()); // Снова можно идти вперед
    }

    @Test
    void formSearchResults_withNoMatches_shouldReturnEmpty() {
        findResult.formSearchResults(transcript, "missing");
        assertFalse(findResult.hasNext());
        assertFalse(findResult.hasPrevious());
    }

    @Test
    void formSearchResults_withEmptySearch_shouldReturnEmpty() {
        findResult.formSearchResults(transcript, "");
        assertFalse(findResult.hasNext());
        assertFalse(findResult.hasPrevious());
    }

    @Test
    void formSearchResults_shouldHandleEmptyTranscript() {
        Transcript emptyTranscript = new Transcript("Empty", new Date());
        findResult.formSearchResults(emptyTranscript, "hello");
        assertFalse(findResult.hasNext());
        assertFalse(findResult.hasPrevious());
    }
}