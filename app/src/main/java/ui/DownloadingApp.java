package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class DownloadingApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        setStage(stage);
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logo.png")));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static Scene getScene() {
        Font.loadFont(DownloadingApp.class.getResourceAsStream("/fonts/TildaSans-Black.ttf"), 12);
        Font.loadFont(DownloadingApp.class.getResourceAsStream("/fonts/TildaSans-Bold.ttf"), 12);
        Font.loadFont(DownloadingApp.class.getResourceAsStream("/fonts/TildaSans-ExtraBold.ttf"), 12);
        Font.loadFont(DownloadingApp.class.getResourceAsStream("/fonts/TildaSans-Light.ttf"), 12);
        Font.loadFont(DownloadingApp.class.getResourceAsStream("/fonts/TildaSans-Medium.ttf"), 12);
        Font.loadFont(DownloadingApp.class.getResourceAsStream("/fonts/TildaSans-Regular.ttf"), 12);
        Font.loadFont(DownloadingApp.class.getResourceAsStream("/fonts/TildaSans-Semibold.ttf"), 12);
        FXMLLoader fxmlLoader = new FXMLLoader(DownloadingApp.class.getResource("/fx_screens/downloading.fxml"));
        try {
            return new Scene(fxmlLoader.load(), 850, 500);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setStage(Stage stage) {
        stage.setScene(getScene());
        stage.setTitle("Извлечение аудио");
    }
}
