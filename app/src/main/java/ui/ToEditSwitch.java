package ui;

import javafx.application.Platform;
import javafx.stage.Stage;
import logic.general.Transcript;
import logic.video_processing.queue.listeners.TranscriptListener;
import logic.video_processing.vosk.analiseDTO.RawTranscript;

public class ToEditSwitch implements TranscriptListener {
    private final Stage stage;

    public ToEditSwitch(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void onResultReady(RawTranscript rawTranscript) {

    }
}
