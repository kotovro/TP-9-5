package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class LoadOptionDialogController {
    @FXML
    private StackPane cardVideo;

    @FXML
    private StackPane cardDatabase;

    @FXML
    private Label label;

    public void setLabelText(String text) {
        label.setText(text);
    }

    @FXML
    private void loadFromDatabase() {
        LoadStenogrammApp.setStage(mainStage);
        Stage currentStage = (Stage) cardVideo.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    private void loadFromVideo() {
        DownloadingApp.setStage(mainStage);
        Stage currentStage = (Stage) cardVideo.getScene().getWindow();
        currentStage.close();
    }

    private Stage mainStage;

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }
}
