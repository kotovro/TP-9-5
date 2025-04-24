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
        Font.loadFont(getClass().getResourceAsStream("/fonts/TildaSans-Black.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/fonts/TildaSans-Bold.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/fonts/TildaSans-ExtraBold.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/fonts/TildaSans-Light.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/fonts/TildaSans-Medium.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/fonts/TildaSans-Regular.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/fonts/TildaSans-Semibold.ttf"), 12);
        FXMLLoader fxmlLoader = new FXMLLoader(DownloadingApp.class.getResource("/fx_screens/downloading.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 850, 500);
        stage.setTitle("Извлечение аудио");
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logo.png")));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
