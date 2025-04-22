package logic.text_edit.action;

import logic.general.Replica;

public class AddText implements StoryPoint {
    private final String text;
    private final Replica replica;
    private final int position;

    public AddText(String text, Replica replica, int position) {
        this.text = text;
        this.replica = replica;
        this.position = position;
    }


    @Override
    public void unapply() {
        replica.getText().insert(position, text);
    }

    @Override
    public void apply() {
        replica.getText().delete(position, position + text.length());
    }
}
