package logic.text_edit.action;

import logic.general.Replica;

public class ReplaceText implements StoryPoint {
    private final Replica replica;
    private final int position;
    private final String oldText;
    private final String newText;

    public ReplaceText(Replica replica, int position, String oldText, String newText) {
        this.replica = replica;
        this.position = position;
        this.oldText = oldText;
        this.newText = newText;
    }

    @Override
    public void apply() {
        replica.getText().replace(position, position + oldText.length(), newText);
    }

    @Override
    public void unapply() {
        replica.getText().replace(position, position + newText.length(), oldText);
    }
}
