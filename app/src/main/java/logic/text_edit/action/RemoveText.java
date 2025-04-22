package logic.text_edit.action;

import logic.general.Replica;

public class RemoveText implements StoryPoint {
    private final Replica replica;
    private final int position;
    private final int length;
    private String removedText;

    public RemoveText(Replica replica, int position, int length) {
        this.replica = replica;
        this.position = position;
        this.length = length;
    }

    @Override
    public void apply() {
        if (removedText == null) removedText = replica.getText().substring(position, position + length);
        replica.getText().delete(position, position + removedText.length());
    }

    @Override
    public void unapply() {
        replica.getText().insert(position, removedText);
    }
}
