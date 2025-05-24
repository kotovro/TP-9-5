package ui;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;
import logic.video_processing.queue.ProcessingQueue;
import ui.custom_elements.DownloadScrollPane;
import ui.custom_elements.ListenProgressBar;
import ui.custom_elements.StatusLabel;
import ui.main_panes.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    private final ProcessingQueue processingQueue = new ProcessingQueue();

    private final EditPane editPane = new EditPane(this, processingQueue);
    private final MainPane mainPane = new MainPane();
    private final LoadPane loadPane = new LoadPane(processingQueue);
    private final SavesPane savesPane = new SavesPane(editPane.getController(), processingQueue);
    private final DialogEdit dialogEdit = new DialogEdit();
    private final DialogExit dialogExit = new DialogExit();
    private final DialogRecord dialogRecord = new DialogRecord();
    DownloadScrollPane downloadScrollPane = new DownloadScrollPane();

    private static final String STYLE = BaseController.class.getResource("/styles/download.css").toExternalForm();
    DialogWindow dialog;


    @FXML
    public void initialize() {
        menuButton.setOnMouseClicked(event -> toggleMenu());
        downloadLane.setOnMouseClicked(event -> toggleDownloads());
        setDownloadPane();
        setDownloadLane();
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
        boolean load = contentPane.getController().load();
        if (load) {
            this.contentPane.getChildren().clear();
            this.contentPane.getChildren().add(contentPane);
            paneController = contentPane.getController();
        }
    }

    public void loadDialog() {
        try {
            dialog = new DialogWindow(menuButton.getScene().getWindow());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dialog.setDialogStage(dialogEdit);
        dialog.show();
    }

    private void toggleDownloads() {
        if (downloadScrollPane.isEmpty()) return;

        isDownloadsOpen = !isDownloadsOpen;
        FadeTransition fade = new FadeTransition(Duration.millis(500), downloadPane);
        if (isDownloadsOpen) {
            downloadPane.setVisible(true);
            downloadLane.setStyle("-fx-background-color: #3A5CB9; -fx-background-radius: 20;");
            fade.setFromValue(0.0);
            fade.setToValue(1.0);

        }
        else {
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setOnFinished(e -> downloadPane.setVisible(isDownloadsOpen));
            downloadLane.setStyle("-fx-background-color:  #B8D0FF; -fx-background-radius: 20;");
        }
        fade.play();
    }

    private void setDownloadLane() {
        ListenProgressBar loadBar = new ListenProgressBar();
        StatusLabel statusLabel = new StatusLabel();
        processingQueue.setProcessListener(loadBar);
        processingQueue.addStatusListener(statusLabel);
        processingQueue.addStatusListener(loadBar);

        loadBar.setOnMouseClicked(event -> toggleDownloads());
        downloadLane.getChildren().addAll(loadBar, statusLabel);
    }

    private void setDownloadPane() {
        downloadPane.getChildren().add(downloadScrollPane);
        processingQueue.setQueueChangeListener(downloadScrollPane);

        downloadPane.prefWidthProperty().bind(downloadScrollPane.widthProperty());
        downloadPane.prefHeightProperty().bind(downloadScrollPane.heightProperty());

        if (downloadPane.getScene() != null) {
            downloadPane.getScene().getStylesheets().add(STYLE);
        } else {
            downloadPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    newScene.getStylesheets().add(STYLE);
                }
            });
        }
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
