package logic.unit_test.general_test;

import logic.general.Replica;
import logic.general.Speaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReplicaTest {

    @Mock
    private Speaker speaker;

    private Replica replica;

    @BeforeEach
    void setUp() {
        replica = new Replica("Sample text", speaker);
    }

    @Test
    void testConstructorWithTextAndSpeaker() {
        assertEquals("Sample text", replica.getText());
        assertEquals(speaker, replica.getSpeaker());
    }

    @Test
    void testDefaultConstructor() {
        Replica emptyReplica = new Replica();
        assertNull(emptyReplica.getText());
        assertNull(emptyReplica.getSpeaker());
    }

    @Test
    void testSetAndGetText() {
        replica.setText("New text");
        assertEquals("New text", replica.getText());
    }

    @Test
    void testSetAndGetSpeaker() {
        Speaker newSpeaker = mock(Speaker.class);
        replica.setSpeaker(newSpeaker);
        assertEquals(newSpeaker, replica.getSpeaker());
    }

    @Test
    void testFindAllOccurrencesEmptySearchText() {
        ArrayList<Integer> result = replica.findAllOccurrences("");
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAllOccurrencesNoMatch() {
        ArrayList<Integer> result = replica.findAllOccurrences("Null");
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAllOccurrencesSingleMatch() {
        replica.setText("Hello world");
        ArrayList<Integer> result = replica.findAllOccurrences("world");
        assertEquals(1, result.size());
        assertEquals(6, result.get(0));
    }

    @Test
    void testFindAllOccurrencesMultipleMatches() {
        replica.setText("test test test");
        ArrayList<Integer> result = replica.findAllOccurrences("test");
        assertEquals(3, result.size());
        assertEquals(0, result.get(0));
        assertEquals(5, result.get(1));
        assertEquals(10, result.get(2));
    }

    @Test
    void testFindAllOccurrencesCaseSensitive() {
        replica.setText("Test TEST test");
        ArrayList<Integer> result = replica.findAllOccurrences("Test");
        assertEquals(1, result.size());
        assertEquals(0, result.get(0));
    }

    @Test
    void testFindAllOccurrencesWithSpecialCharacters() {
        replica.setText("Hello! World? Hello.");
        ArrayList<Integer> result = replica.findAllOccurrences("Hello");
        assertEquals(2, result.size());
        assertEquals(0, result.get(0));
        assertEquals(14, result.get(1));
    }

    @Test
    void testFindAllOccurrencesWithOverlappingMatches() {
        replica.setText("aaaa");
        ArrayList<Integer> result = replica.findAllOccurrences("aa");
        assertEquals(3, result.size());
        assertEquals(0, result.get(0));
        assertEquals(1, result.get(1));
    }

    @Test
    void testSetNullText() {
        replica.setText(null);
        assertNull(replica.getText());
    }
}
