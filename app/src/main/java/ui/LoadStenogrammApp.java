package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class LoadStenogrammApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EditWindow.class.getResource("/fx_screens/loadStenogramm.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 610, 450);
        //String stylesheet = getClass().getResource("/styles/slyles.css").toExternalForm();
        //scene.getStylesheets().add(stylesheet);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
