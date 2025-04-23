package logic.text_edit.action;

import logic.general.Replica;

public class RemoveText implements Executable {
    private Replica replica;
    private int position;
    private int length;
    private String removedText;

    public RemoveText(Replica replica, int position, int length) {
        this.replica = replica;
        this.position = position;
        this.length = length;
    }

    @Override
    public void apply() {
        String original = replica.getText();
        removedText = original.substring(position, position + length);
        String newText = original.substring(0, position) + original.substring(position + length);
        replica.setText(newText);
    }

    @Override
    public void unapply() {
        String current = replica.getText();
        String reverted = current.substring(0, position) + removedText + current.substring(position);
        replica.setText(reverted);
    }
}
