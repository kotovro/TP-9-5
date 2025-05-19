package ui;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import ui.main_panes.*;

import java.io.IOException;
import java.util.Objects;

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
    @FXML
    private ImageView fonImage;
    @FXML
    private Pane editMenu;
    @FXML
    private Button exitMenu;

    private boolean isMenuOpen = false;
    private boolean isDownloadsOpen = false;
    private final int MENU_WIDTH = 363;
    private PaneController paneController;
    private PaneController DialogController;

    //Их лучше создавать не здесь
    private final EditPane editPane = new EditPane(this);
    private final MainPane mainPane = new MainPane();
    private final LoadPane loadPane = new LoadPane();
    private final SavesPane savesPane = new SavesPane();
    private final DialogEdit dialogEdit = new DialogEdit();
    private final DialogExit dialogExit = new DialogExit();
    private final DialogRecord dialogRecord = new DialogRecord();
    DialogWindow dialog;


    @FXML
    public void initialize() {
        menuButton.setOnMouseClicked(event -> toggleMenu());
        downloadLane.setOnMouseClicked(event -> toggleDownloads());
        setContent(mainPane);

    }

    @FXML
    private void mainClick() {
        toggleMenu();
        Image image = new Image(Objects.requireNonNull(getClass().getResource("/images/fon1.png")).toExternalForm());
        fonImage.setImage(image);
        setContent(mainPane);
    }

    @FXML
    private void editClick() {
        paneController.load();
        toggleMenu();
        changeImage();
        setContent(editPane);
    }

    @FXML
    private void loadClick() {
        toggleMenu();
        changeImage();
        setContent(loadPane);
    }

    @FXML
    private void savedClick() {
        toggleMenu();
        changeImage();
        setContent(savesPane);
    }

    @FXML
    private void exitClick() {
        try {
            dialog = new DialogWindow(exitMenu.getScene().getWindow());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dialog.setDialogStage(dialogExit);
        dialog.show();
    }

    public void setContent(ContentPane contentPane) {
        this.contentPane.getChildren().clear();
        this.contentPane.getChildren().add(contentPane);
        paneController = contentPane.getController();
    }

    public void loaddialog() {
        try {
            dialog = new DialogWindow(editMenu.getScene().getWindow());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dialog.setDialogStage(dialogEdit);
        dialog.show();
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
            disablePane.setVisible(true);
            paneController.stopAnimation();
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

    private void changeImage() {
        Image image = new Image(Objects.requireNonNull(getClass().getResource("/images/fon2.png")).toExternalForm());
        fonImage.setImage(image);
    }
}
