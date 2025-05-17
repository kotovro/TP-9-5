package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Saving {
    public static void createDialog(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(DownloadingApp.class.getResource("/fx_screens/saving.fxml"));
            Scene scene = new Scene(loader.load(), 400, 200);
            Stage dialog = new Stage();
            dialog.setResizable(false);
            dialog.setTitle("Сохранение");
            dialog.initOwner(primaryStage);
            dialog.setScene(scene);

            SavingController controller = loader.getController();
            controller.setParentStage(primaryStage);
            dialog.show();
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
        }
    }
}