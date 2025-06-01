package logic.unit_test.general_test;

import logic.general.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TagTest {

    private Tag tag;

    @BeforeEach
    void setUp() {
        tag = new Tag("SampleTag");
    }

    @Test
    void testConstructorAndGetName() {
        assertEquals("SampleTag", tag.getName());
        assertEquals(-1, tag.getId());
    }

    @Test
    void testSetAndGetId() {
        tag.setId(1);
        assertEquals(1, tag.getId());
    }

    @Test
    void testSetAndGetName() {
        tag.setName("NewTag");
        assertEquals("NewTag", tag.getName());
    }
}
