package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class EditWindow extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EditWindow.class.getResource("/fx_screens/EditView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        String stylesheet = getClass().getResource("/styles/slyles.css").toExternalForm();
        scene.getStylesheets().add(stylesheet);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Scene getScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EditWindow.class.getResource("/fx_screens/EditView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        String stylesheet = EditWindow.class.getResource("/styles/slyles.css").toExternalForm();
        scene.getStylesheets().add(stylesheet);
        return scene;
    }
}