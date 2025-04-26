package ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.general.Transcript;

import java.io.IOException;

public class EditWindow {
    public static Scene getScene(Transcript transcript) {
        FXMLLoader fxmlLoader = new FXMLLoader(EditWindow.class.getResource("/fx_screens/EditView.fxml"));
        fxmlLoader.setController(new EditController(transcript));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 450);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String stylesheet = EditWindow.class.getResource("/styles/slyles.css").toExternalForm();
        scene.getStylesheets().add(stylesheet);
        return scene;
    }

    public static void setStage(Stage stage, Transcript transcript) {
        stage.setScene(getScene(transcript));
        stage.setTitle("Загрузка стенограммы");
    }
}