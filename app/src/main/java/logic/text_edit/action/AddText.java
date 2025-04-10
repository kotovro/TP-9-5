package logic.text_edit.action;

import logic.general.Replica;

public class AddText implements Executable {
    private String text;
    private Replica replica;
    private int position;

    public AddText(String text, Replica replica, int position) {
        this.text = text;
        this.replica = replica;
        this.position = position;
    }


    @Override
    public void unapply() {

    }

    @Override
    public void apply() {
        replica.setText(replica.getText().substring(0, position) + text + replica.getText().substring(position + 1, text.length()));
    }
}
