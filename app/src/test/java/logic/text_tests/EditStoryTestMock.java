package logic.text_tests;

import logic.text_edit.EditStory;
import logic.text_edit.action.StoryPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.DisplayName;

class EditStoryTestMock {
    private EditStory editStory;
    private StoryPoint mockAction1;
    private StoryPoint mockAction2;
    private int BUFFER_MAX_SIZE = 100;

    @BeforeEach
    void setUp() {
        editStory = new EditStory();
        mockAction1 = mock(StoryPoint.class);
        mockAction2 = mock(StoryPoint.class);
    }

    @Test
    @DisplayName("Изначально возможность отмены действия недоступна")
    void testCanUndoInitiallyFalse() {
        assertFalse(editStory.canUndo());
    }

    @Test
    @DisplayName("Изначально возможность повторного выполнения отмененного действия недоступна")
    void testCanRedoInitiallyFalse() {
        assertFalse(editStory.canRedo());
    }

    @Test
    @DisplayName("Добавление действия, выполнение отмены и проверка состояния")
    void testAddActionAndUndo() {
        editStory.addLast(mockAction1);
        assertTrue(editStory.canUndo(), "После добавления действия, undo доступен");
        assertFalse(editStory.canRedo(), "После добавления действия, redo недоступен");

        editStory.undoLast();
        verify(mockAction1).unapply();
        assertFalse(editStory.canUndo(), "После отмены, undo недоступен");
        assertTrue(editStory.canRedo(), "После отмены, redo доступен");
    }

    @Test
    @DisplayName("Повторное выполнение отмененного действия после undo")
    void testRedoAfterUndo() {
        editStory.addLast(mockAction1);
        editStory.undoLast();

        editStory.redoLast();
        verify(mockAction1).apply();
        assertTrue(editStory.canUndo(), "После redo, undo доступен");
        assertFalse(editStory.canRedo(), "После redo, redo недоступен");
    }

    @Test
    @DisplayName("Добавление нескольких действий и последовательные undo и redo")
    void testMultipleActions() {
        editStory.addLast(mockAction1);
        editStory.addLast(mockAction2);

        assertTrue(editStory.canUndo(), "После добавления двух действий, undo доступен");
        assertFalse(editStory.canRedo(), "После добавления двух действий, redo недоступен");

        editStory.undoLast();
        verify(mockAction2).unapply();
        assertTrue(editStory.canUndo(), "После undo, еще есть действия для отмены");
        assertTrue(editStory.canRedo(), "После undo, redo доступен");

        editStory.undoLast();
        verify(mockAction1).unapply();
        assertFalse(editStory.canUndo(), "После второго undo, undo недоступен");
        assertTrue(editStory.canRedo(), "После второго undo, redo доступен");

        editStory.redoLast();
        verify(mockAction1).apply();
        assertTrue(editStory.canUndo(), "После redo, undo доступен");
        assertTrue(editStory.canRedo(), "После redo, redo доступен");
    }

    @Test
    @DisplayName("Проверка поведения буфера при переполнении")
    void testBufferOverflow() {
        for (int i = 0; i < BUFFER_MAX_SIZE * 2 + 1; i++) {
            StoryPoint mockAction = mock(StoryPoint.class);
            editStory.addLast(mockAction);
        }

        assertTrue(editStory.canUndo(), "После переполнения буфера, undo доступен");
        while (editStory.canUndo()) {
            editStory.undoLast();
        }
        assertFalse(editStory.canUndo());
    }

    @Test
    @DisplayName("Метод undo вызывается, когда действий для отмены нет")
    void testUndoWhenNoActionsDoesNothing() {
        assertFalse(editStory.canUndo(), "При пустом стеке, undo недоступен");
        editStory.undoLast();
    }

    @Test
    @DisplayName("Метод redo вызывается, когда отмененных действий нет")
    void testRedoWhenNoActionsDoesNothing() {
        assertFalse(editStory.canRedo(), "При отсутствии отмененных действий, redo недоступен");
        editStory.redoLast();
    }
}