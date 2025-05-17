package ui.custom_elements;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;
import logic.video_processing.audio_extractor.AudioExtractorStreamer;
import logic.video_processing.audio_extractor.ProcessListener;
import logic.video_processing.queue.Processor;

public class ListenProgressBar extends ProgressBar implements ProcessListener {
    private Processor processor;
    Task<Void> requests;

    public ListenProgressBar() {
        setProgress(0);
    }

    @Override
    public void notifyStart() {
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

        this.progressProperty().bind(requests.progressProperty());
        new Thread(requests).start();
    }

    @Override
    public void notifyStop() {
        requests.cancel();
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }
}
