package ui.hotkeys;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import logic.text_edit.EditStory;

public class HotkeysProvider {
    private final EditStory story;
    private final IAction findAction = new FindAction();
    private final IAction undoAction;
    private final IAction redoAction;

    public HotkeysProvider(EditStory story) {
        this.story = story;
        undoAction = new UndoAction(story);
        redoAction = new RedoAction(story);
    }

    public void attachHotkeys(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN).match(event)) {
                findAction.execute();
            } else if (new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN).match(event)) {
                if (story.canUndo()) undoAction.execute();
            } else if (new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN).match(event)) {
                if (story.canRedo()) redoAction.execute();
            } else if (event.getCode() == KeyCode.DELETE) {

            }
        });
    }

}
