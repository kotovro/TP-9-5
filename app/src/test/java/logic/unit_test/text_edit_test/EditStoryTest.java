package logic.unit_test.text_edit_test;

import logic.text_edit.action.StoryPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EditStoryTest {
    private logic.text_edit.EditStory editStory;
    private StoryPoint action;

    @BeforeEach
    void setUp() {
        editStory = new logic.text_edit.EditStory();
        action = mock(StoryPoint.class);
    }

    @Test
    @DisplayName("Проверка начального состояния undo")
    void testCannotUndoInitially() {
        assertFalse(editStory.canUndo(), "Изначально undo недоступно");
    }

    @Test
    @DisplayName("Проверка начального состояния redo")
    void testCannotRedoInitially() {
        assertFalse(editStory.canRedo(), "Изначально redo недоступно");
    }

    @Test
    @DisplayName("Проверка добавления и отмены действия")
    void testAddAndUndo() {
        editStory.addLast(action);
        assertTrue(editStory.canUndo(), "Undo должно быть доступно после добавления");
        editStory.undoLast();
        verify(action).unapply();
        assertFalse(editStory.canUndo(), "Undo недоступно после отмены");
    }

    @Test
    @DisplayName("Проверка redo действия")
    void testRedo() {
        editStory.addLast(action);
        editStory.undoLast();
        assertTrue(editStory.canRedo(), "Redo должно быть доступно после undo");
        editStory.redoLast();
        verify(action).apply();
        assertFalse(editStory.canRedo(), "Redo недоступно после повторного выполнения");
    }

    @Test
    @DisplayName("Проверка очистки буфера при превышении размера")
    void testBufferOverflow() {
        for (int i = 0; i < 200; i++) {
            editStory.addLast(mock(StoryPoint.class));
        }
        assertTrue(editStory.canUndo(), "Undo должно быть доступно");
        assertEquals(100, editStory.canUndo() ? 100 : 0, "Буфер должен содержать 100 действий");
    }
}