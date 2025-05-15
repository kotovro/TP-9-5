package ui;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class LaneController {

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label progressLabel;

    @FXML
    private Label label; //Виталь, лабел для твоих состояний

    @FXML
    private void initialize() {
        startLoadingTask();
    }

    private void startLoadingTask() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (int i = 0; i <= 100; i++) {
                    Thread.sleep(50); // Имитация загрузки
                    updateProgress(i, 100); // Обновляем прогресс (0-1)
                }
                return null;
            }
        };

        progressBar.progressProperty().bind(task.progressProperty());
        progressLabel.textProperty().bind(
                task.progressProperty().multiply(100).asString("%.0f%%")
        );

        new Thread(task).start();
    }
}