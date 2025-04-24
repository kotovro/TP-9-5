package ui.hotkeys;

import logic.general.Transcript;

public class DeleteAction implements IAction {
    Transcript transcript;
    public DeleteAction(Transcript transcript) {
        this.transcript = transcript;
    }

    @Override
    public void execute() {
        transcript.removeReplica(transcript.getCurrentIndex());
    }
}
