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
import ui.main_panes.*;

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
    private PaneController paneController;

    //Их лучше создавать не здесь
    private final EditPain editPain = new EditPain();
    private final MainPain mainPain = new MainPain();
    private final LoadPane loadPane = new LoadPane();
    private final SavesPain savesPain = new SavesPain();

    public void setContent(ContentPane contentPane) {
        this.contentPane = contentPane;
        paneController = contentPane.getController();
    }

    @FXML
    public void initialize() {
        menuButton.setOnMouseClicked(event -> toggleMenu());
        downloadLane.setOnMouseClicked(event -> toggleDownloads());
        setContent(new MainPain());
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
        if (isMenuOpen) {
            paneController.stopAnimation();
            disablePane.setVisible(true);
        }
        else {
            disablePane.setVisible(false);
            paneController.startAnimation();
        }
        TranslateTransition animation = new TranslateTransition(Duration.millis(300), sideMenu);
        animation.setFromX(isMenuOpen ? -MENU_WIDTH : 0);
        animation.setToX(isMenuOpen ? 0 : -MENU_WIDTH);
        animation.play();
    }
}
