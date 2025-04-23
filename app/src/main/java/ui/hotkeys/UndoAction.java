package ui.hotkeys;

import logic.text_edit.EditStory;

public class UndoAction implements IAction {
    private final EditStory story;
    public UndoAction(EditStory story) {
        this.story = story;
    }

    @Override
    public void execute() {
        story.undoLast();
    }

}
