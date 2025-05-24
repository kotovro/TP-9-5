package ui.custom_elements;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import logic.video_processing.ProcessStatus;
import logic.video_processing.queue.listeners.StatusListener;

public class StatusLabel extends Label implements StatusListener {
    public StatusLabel() {
        super("Ожидание начала");
        Font manropeFont2 = Font.loadFont(getClass().getResourceAsStream("/fonts/Manrope-Regular.ttf"), 16);
        setStyle("-fx-font-family: \"Manrope Regular\"; -fx-font-size: 12; -fx-text-fill: #131F5A;");

        setFont(manropeFont2);
        setLayoutX(38);
        setLayoutY(20);
    }

    @Override
    public void onStatusChanged(ProcessStatus status) {
        Platform.runLater(() -> {
            this.setText(status.getMessage());
        });
    }
}
