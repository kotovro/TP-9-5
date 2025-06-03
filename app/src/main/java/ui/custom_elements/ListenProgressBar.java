package ui.custom_elements;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;
import logic.video_processing.ProcessStatus;
import logic.video_processing.audio_extractor.AudioExtractorStreamer;
import logic.video_processing.audio_extractor.ProcessListener;
import logic.video_processing.queue.Processor;
import logic.video_processing.queue.listeners.StatusListener;

public class ListenProgressBar extends ProgressBar implements ProcessListener, StatusListener {
    private Task<Void> requests;

    public ListenProgressBar() {
        setPrefSize(317, 14);
        setProgress(0);
        setStyle("-fx-control-inner-background: #B8D0FF; -fx-accent: #131F5A; -fx-background-radius: 6;" +
                "-fx-background-color: transparent; -fx-background-insets: 0; -fx-effect: none; -fx-padding: 0; " +
                "-fx-border-insets: 0;");
        setLayoutY(5);
        setLayoutX(14);
    }

    @Override
    public void notifyStart(Processor processor) {
        requests = new Task<>() {
            @Override
            protected Void call() throws Exception {
                while (true) {
                    Thread.sleep(50);
                    Platform.runLater(() -> {
                        updateProgress(processor.getProcessPercent(), 100);
                    });
                }
            }
        };

        Platform.runLater(() -> {
            this.progressProperty().bind(requests.progressProperty());
        });
        new Thread(requests).start();
    }

    @Override
    public void notifyStop(Processor processor) {
        this.progressProperty().unbind();
        requests.cancel();
    }

    @Override
    public void onStatusChanged(ProcessStatus status) {
        Platform.runLater(() -> {
            if (status == ProcessStatus.WAITING_FOR_START || status == ProcessStatus.TASK_FAILED || status == ProcessStatus.TASK_FINISHED) {
                setProgress(0);
            } else if (status == ProcessStatus.MODEL_UNLOAD || status == ProcessStatus.MODEL_UPLOAD) {
                setProgress(-1);
            }
        });
    }
}
