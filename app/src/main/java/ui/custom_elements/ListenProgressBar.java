package ui.custom_elements;

import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;
import logic.video_processing.audio_extractor.AudioExtractorStreamer;
import logic.video_processing.audio_extractor.ProcessListener;

public class ListenProgressBar extends ProgressBar implements ProcessListener {
    private final AudioExtractorStreamer audioExtractor = new AudioExtractorStreamer();
    Task<Void> requests;

    public ListenProgressBar() {
        audioExtractor.subscribe(this);
        setProgress(0);
    }

    @Override
    public void notifyStart() {
        requests = new Task<>() {
            @Override
            protected Void call() throws Exception {
                while (true) {
                    Thread.sleep(50);
                    updateProgress(audioExtractor.getProcessPercent(), 100);
                    System.out.println("I work");
                }
            }
        };

        this.progressProperty().bind(requests.progressProperty());
        new Thread(requests).start();
    }

    @Override
    public void notifyStop() {
        requests.cancel();
        System.out.println("I stop");
    }

    public AudioExtractorStreamer getAudioExtractor() {
        return audioExtractor;
    }
}
