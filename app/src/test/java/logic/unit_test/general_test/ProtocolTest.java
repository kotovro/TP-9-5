package logic.unit_test.general_test;

import logic.general.Protocol;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProtocolTest {

    @Test
    void testConstructorWithTranscriptIdAndText() {
        Protocol protocol = new Protocol(1, "Sample text");
        assertEquals(1, protocol.getTranscriptId());
        assertEquals("Sample text", protocol.getText());
    }

    @Test
    void testConstructorWithTextOnly() {
        Protocol protocol = new Protocol("Sample text");
        assertEquals(0, protocol.getTranscriptId());
        assertEquals("Sample text", protocol.getText());
    }

    @Test
    void testGetTextWithNullText() {
        Protocol protocol = new Protocol(1, null);
        assertNull(protocol.getText());
    }
}