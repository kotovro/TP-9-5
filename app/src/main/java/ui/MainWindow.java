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
    public static final Font manropeBold = Font.loadFont(MainWindow.class.getResourceAsStream("/fonts/Manrope-Bold.ttf"), 12);

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

        Scene scene;

        Font.loadFont(MainWindow.class.getResourceAsStream("/fonts/Manrope-Bold.ttf"), 12);
        Font.loadFont(MainWindow.class.getResourceAsStream("/fonts/Manrope-ExtraBold.ttf"), 12);
        Font.loadFont(MainWindow.class.getResourceAsStream("/fonts/Manrope-Light.ttf"), 12);
        Font.loadFont(MainWindow.class.getResourceAsStream("/fonts/Manrope-ExtraLight.ttf"), 12);
        Font.loadFont(MainWindow.class.getResourceAsStream("/fonts/Manrope-Medium.ttf"), 12);
        Font.loadFont(MainWindow.class.getResourceAsStream("/fonts/Manrope-Regular.ttf"), 12);
        Font.loadFont(MainWindow.class.getResourceAsStream("/fonts/Manrope-SemiBold.ttf"), 12);
        Font.loadFont(MainWindow.class.getResourceAsStream("/fonts/Fyodor-BoldExpanded.ttf"), 12);

        try {
            scene = new Scene(fxmlLoader.load(), 1137, 778);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return scene;
    }

    public static void setStage(Stage stage) {
        stage.setScene(getScene());
        stage.setTitle("Встречеслав");
    }
}
