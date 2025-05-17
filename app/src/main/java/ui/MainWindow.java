package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindow extends Application {
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
        FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/fx_screens/base.fxml"));
        try {
            return new Scene(fxmlLoader.load(), 1137, 778);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setStage(Stage stage) {
        stage.setScene(getScene());
        stage.setTitle("Встречеслав");
    }
}
