package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import logic.general.Speaker;
import logic.persistence.DBManager;

import java.io.IOException;
import java.util.List;

import static ui.EditController.getImage;

public class DownloadingApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        setStage(stage);
        mockInit();
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

    private void mockInit() {
        List<Speaker> speakers = List.of(
                new Speaker("Не выбран", getImage("/images/UserSpeak2.png"), 0),
                new Speaker("Anna", getImage("/images/logo.png"), 1),
                new Speaker("Viktor", getImage("/images/UserSpeak2.png"), 2),
                new Speaker("Galina", getImage("/images/DangerCircle.png"), 3)
        );
        for (Speaker speaker : speakers) {
            DBManager.getSpeakerDao().addSpeaker(speaker);
        }
    }
}
