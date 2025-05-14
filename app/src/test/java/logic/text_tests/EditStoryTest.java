package logic.text_tests;

import logic.text_edit.EditStory;
import logic.text_edit.action.StoryPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EditStoryTest {
    private EditStory editStory;
    private TestStoryPoint action1;
    private TestStoryPoint action2;
    private final int BUFFER_MAX_SIZE = 100;

    // Простая реализация StoryPoint для тестов
    private static class TestStoryPoint implements StoryPoint {
        public int applyCount = 0;
        public int unapplyCount = 0;

        @Override
        public void apply() {
            applyCount++;
        }

        @Override
        public void unapply() {
            unapplyCount++;
        }
    }

    @BeforeEach
    void setUp() {
        editStory = new EditStory();
        action1 = new TestStoryPoint();
        action2 = new TestStoryPoint();
    }

    @Test
    void canUndo_shouldReturnFalseInitially() {
        assertFalse(editStory.canUndo());
    }

    @Test
    void canRedo_shouldReturnFalseInitially() {
        assertFalse(editStory.canRedo());
    }

    @Test
    void addLast_shouldAddActionAndEnableUndo() {
        editStory.addLast(action1);
        assertTrue(editStory.canUndo());
        assertFalse(editStory.canRedo());
    }

    @Test
    void undoLast_shouldCallUnapplyAndUpdateState() {
        editStory.addLast(action1);
        editStory.undoLast();

        assertEquals(1, action1.unapplyCount);
        assertFalse(editStory.canUndo());
        assertTrue(editStory.canRedo());
    }

    @Test
    void redoLast_shouldCallApplyAndUpdateState() {
        editStory.addLast(action1);
        editStory.undoLast();
        editStory.redoLast();

        assertEquals(1, action1.applyCount);
        assertTrue(editStory.canUndo());
        assertFalse(editStory.canRedo());
    }

    @Test
    void multipleActions_shouldWorkCorrectly() {
        editStory.addLast(action1);
        editStory.addLast(action2);

        editStory.undoLast();
        assertEquals(1, action2.unapplyCount);
        assertTrue(editStory.canUndo());
        assertTrue(editStory.canRedo());

        editStory.undoLast();
        assertEquals(1, action1.unapplyCount);
        assertFalse(editStory.canUndo());
        assertTrue(editStory.canRedo());

        editStory.redoLast();
        assertEquals(1, action1.applyCount);
        assertTrue(editStory.canUndo());
        assertTrue(editStory.canRedo());
    }

    @Test
    void undoLast_whenNoActions_shouldDoNothing() {
        editStory.undoLast();
        assertFalse(editStory.canUndo());
    }

    @Test
    void redoLast_whenNoActions_shouldDoNothing() {
        editStory.redoLast();
        assertFalse(editStory.canRedo());
    }

    @Test
    void bufferOverflow_shouldMaintainCorrectState() {
        for (int i = 0; i < BUFFER_MAX_SIZE * 2 + 5; i++) {
            editStory.addLast(new TestStoryPoint());
        }

        assertTrue(editStory.canUndo(), "После переполнения буфера, undo доступен");
        while (editStory.canUndo()) {
            editStory.undoLast();
        }
        assertFalse(editStory.canUndo());
    }
}
