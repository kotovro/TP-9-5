package ui;

import javafx.application.Platform;
import javafx.stage.Stage;
import logic.general.Transcript;
import logic.video_processing.queue.ResultListener;

public class ToEditSwitch implements ResultListener {
    private final Stage stage;

    public ToEditSwitch(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void onResultReady(Transcript transcript) {
        Platform.runLater(() -> {
            EditWindow.setStage(stage, transcript);
        });
    }
}
