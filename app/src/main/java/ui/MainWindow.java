package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainWindow extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Загружаем FXML файл
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx_screens/mainWindow.fxml"));
        Parent root = loader.load();

        // Настраиваем сцену
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Боковое меню с FXML");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}