package ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MainWindow extends Application {
    public static final Font manropeBold = Font.loadFont(MainWindow.class.getResourceAsStream("/fonts/Manrope-Bold.ttf"), 12);
    private TrayIcon trayIcon;
    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        setStage(stage);
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logo.png")));
        setupTrayIcon(stage);
        stage.show();
    }


    private void setupTrayIcon(Stage stage) {
        if (!SystemTray.isSupported()) {
            System.out.println("Трей не поддерживается!");
            return;
        }

        SystemTray tray = SystemTray.getSystemTray();
        java.awt.Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/tray.png"));

        PopupMenu popup = new PopupMenu();
        MenuItem openItem = new MenuItem("Открыть");
        MenuItem exitItem = new MenuItem("Выход");

        ActionListener listener = e -> {
            if (e.getSource() == openItem) {
                Platform.runLater(stage::show);
            } else if (e.getSource() == exitItem) {
                Platform.exit();
                tray.remove(trayIcon);
                System.exit(0);
            }
        };

        openItem.addActionListener(listener);
        exitItem.addActionListener(listener);
        popup.add(openItem);
        popup.add(exitItem);

        trayIcon = new TrayIcon(image, "Встречеслав", popup);
        trayIcon.setImageAutoSize(true);

        trayIcon.addActionListener(e -> Platform.runLater(() -> {
            if (primaryStage != null) {
                primaryStage.show();
                primaryStage.toFront();
            }
        }));

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.err.println("Не удалось добавить иконку в трей");
        }



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
