package logic.unit_test.general_test;

import javafx.scene.image.Image;
import logic.general.Speaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpeakerTest {

    @Mock
    private Image image;

    private Speaker speaker;

    @BeforeEach
    void setUp() {
        speaker = new Speaker("John Smith", image, 1);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals("John Smith", speaker.getName());
        assertEquals(image, speaker.getImage());
        assertEquals(1, speaker.getId());
    }

    @Test
    void testSetId() {
        speaker.setId(2);
        assertEquals(2, speaker.getId());
    }

    @Test
    void testToString() {
        assertEquals("John Smith", speaker.toString());
    }

    @Test
    void testEqualsSameId() {
        Speaker otherSpeaker = new Speaker("Jane Smith", mock(Image.class), 1);
        assertTrue(speaker.equals(otherSpeaker));
    }

    @Test
    void testEqualsDifferentId() {
        Speaker otherSpeaker = new Speaker("Jane Smith", mock(Image.class), 2);
        assertFalse(speaker.equals(otherSpeaker));
    }

    @Test
    void testEqualsNonSpeaker() {
        assertFalse(speaker.equals(new Object()));
    }
}
