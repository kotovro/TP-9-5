package ui;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;

public class BaseController {
    @FXML
    private Pane contentPane;
    @FXML
    private Group menuButton;
    @FXML
    private VBox sideMenu;
    @FXML
    private Pane disablePane;
    @FXML
    private Pane downloadLane;
    @FXML
    private Pane downloadPane;
    private boolean isMenuOpen = false;
    private boolean isDownloadsOpen = false;
    private final int MENU_WIDTH = 363;
    private MainWindowController mainWindowController;

    public void setContent(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx_screens/" + fxml));
            Node node = loader.load();
            contentPane.getChildren().setAll(node);

            if (fxml.equals("mainWindow.fxml")) {
                mainWindowController = loader.getController();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        menuButton.setOnMouseClicked(event -> toggleMenu());
        downloadLane.setOnMouseClicked(event -> toggleDownloads());
        setContent("mainWindow.fxml");
    }

    private void toggleDownloads() {
        isDownloadsOpen = !isDownloadsOpen;
        FadeTransition fade = new FadeTransition(Duration.millis(500), downloadPane);
        if (isDownloadsOpen) {
            downloadPane.setVisible(true);
            fade.setFromValue(0.0);
            fade.setToValue(1.0);

        }
        else {
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setOnFinished(e -> downloadPane.setVisible(false));
        }
        fade.play();
    }

    private void toggleMenu() {
        isMenuOpen = !isMenuOpen;
        if (mainWindowController != null && isMenuOpen) {
            mainWindowController.stopAnimation();
        }
        else if (mainWindowController != null && !isMenuOpen) {
            mainWindowController.startImageSwitching();
        }
        if (isMenuOpen) {
            disablePane.setVisible(true);
        }
        else {
            disablePane.setVisible(false);
        }
        TranslateTransition animation = new TranslateTransition(Duration.millis(300), sideMenu);
        animation.setFromX(isMenuOpen ? -MENU_WIDTH : 0);
        animation.setToX(isMenuOpen ? 0 : -MENU_WIDTH);
        animation.play();
    }
}
