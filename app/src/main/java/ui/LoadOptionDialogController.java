package ui;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class LoadOptionDialogController {
    @FXML
    private javafx.scene.layout.StackPane cardVideo;

    @FXML
    private javafx.scene.layout.StackPane cardDatabase;

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