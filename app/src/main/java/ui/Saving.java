package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Saving extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fx_screens/saving.fxml"));
        primaryStage.setResizable(false);
        Scene scene = new Scene(root, 400, 200);
        primaryStage.setTitle("Saving");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void createDialog(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(Saving.class.getResource("/fx_screens/saving.fxml"));
            Scene scene = new Scene(root, 400, 160);
            Stage dialog = new Stage();
            dialog.setResizable(false);
            dialog.setTitle("Сохранение");
            dialog.initOwner(primaryStage);
            dialog.setScene(scene);
            dialog.show();
        } catch (Exception ignored) {

        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}