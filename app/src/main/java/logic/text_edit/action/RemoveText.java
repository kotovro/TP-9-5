package logic.text_edit.action;

import logic.general.Replica;

public class RemoveText implements StoryPoint {
    private final Replica replica;
    private final int position;
    private final String removedText;

    public RemoveText(Replica replica, int position, String removedText) {
        this.replica = replica;
        this.position = position;
        this.removedText = removedText;
    }

    @Override
    public void apply() {
        replica.getText().delete(position, position + removedText.length());
    }

    @Override
    public void unapply() {
        replica.getText().insert(position, removedText);
    }
}
