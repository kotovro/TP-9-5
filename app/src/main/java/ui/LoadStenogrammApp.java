package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import logic.general.Transcript;

import java.io.IOException;

public class LoadStenogrammApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EditWindow.class.getResource("/fx_screens/loadStenogramm.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 610, 470);
        String stylesheet = getClass().getResource("/styles/load.css").toExternalForm();
        scene.getStylesheets().add(stylesheet);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Scene getScene() {
        FXMLLoader fxmlLoader = new FXMLLoader(EditWindow.class.getResource("/fx_screens/loadStenogramm.fxml"));
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
