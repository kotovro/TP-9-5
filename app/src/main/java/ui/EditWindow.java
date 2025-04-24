package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.general.Transcript;

import java.io.IOException;

public class EditWindow extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EditWindow.class.getResource("/fx_screens/EditView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        String stylesheet = getClass().getResource("/styles/slyles.css").toExternalForm();
        scene.getStylesheets().add(stylesheet);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Scene getScene(Transcript transcript) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EditWindow.class.getResource("/fx_screens/EditView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        String stylesheet = EditWindow.class.getResource("/styles/slyles.css").toExternalForm();
        scene.getStylesheets().add(stylesheet);
        EditController controller = fxmlLoader.getController();
        controller.setTranscript(transcript);
        return scene;
    }
}