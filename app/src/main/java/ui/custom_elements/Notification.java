package ui.custom_elements;

import javafx.application.Platform;
import javafx.scene.control.Label;
import logic.video_processing.ProcessStatus;
import logic.video_processing.queue.listeners.StatusListener;

public class Notification extends Label implements StatusListener {
    public Notification() {
        setText("Ожидание загрузки");
    }

    @Override
    public void onStatusChanged(ProcessStatus status) {
        Platform.runLater(() -> {
            setText(status.getMessage());
        });
    }
}
