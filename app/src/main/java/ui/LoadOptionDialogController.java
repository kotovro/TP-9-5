package ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoadOptionDialogController {
    @FXML
    private javafx.scene.layout.StackPane cardVideo;

    @FXML
    private javafx.scene.layout.StackPane cardDatabase;

    @FXML
    private void loadFromDatabase() {
        openScene("/fx_screens/loadStenogramm.fxml");
    }

    @FXML
    private void loadFromVideo() {
        openScene("/fx_screens/downloading.fxml");
    }

    private Stage mainStage;

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    private void openScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene newScene = new Scene(loader.load());
            Stage newStage = new Stage();
            newStage.setScene(newScene);
            newStage.setTitle("Загрузка стенограммы");
            newStage.show();

            Stage currentStage = (Stage) cardVideo.getScene().getWindow();
            currentStage.close();

            if (mainStage != null) {
                mainStage.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}