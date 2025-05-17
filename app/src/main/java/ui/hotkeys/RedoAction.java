package ui.hotkeys;

import logic.text_edit.EditStory;

public class RedoAction implements IAction {
    private final EditStory story;
    public RedoAction(EditStory story) {
        this.story = story;
    }

    @Override
    public void execute() {
        story.redoLast();
    }

}
