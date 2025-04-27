package ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class LoadStenogrammApp {
    public static Scene getScene() {
        FXMLLoader fxmlLoader = new FXMLLoader(LoadStenogrammApp.class.getResource("/fx_screens/loadStenogramm.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 610, 470);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String stylesheet = LoadStenogrammApp.class.getResource("/styles/load.css").toExternalForm();
        scene.getStylesheets().add(stylesheet);
        return scene;
    }

    public static void setStage(Stage stage) {
        stage.setScene(getScene());
        stage.setTitle("Загрузка стенограммы");
    }
}
