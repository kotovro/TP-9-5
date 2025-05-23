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
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;
import ui.main_panes.*;

import java.io.IOException;
import java.util.Objects;

public class BaseController {
    @FXML
    private Pane contentPane;
    @FXML
    private ScrollPane downloadScroll;
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

    private final EditPane editPane = new EditPane(this);
    private final MainPane mainPane = new MainPane();
    private final LoadPane loadPane = new LoadPane();
    private final SavesPane savesPane = new SavesPane();
    private final DialogEdit dialogEdit = new DialogEdit();
    private final DialogExit dialogExit = new DialogExit();
    private final DialogRecord dialogRecord = new DialogRecord();

    private static final double MAX_HEIGHT_WITHOUT_SCROLL = 332;
    private static final String STYLE = TranscriptDisplayer.class.getResource("/styles/download.css").toExternalForm();
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
            fade.setOnFinished(e -> downloadPane.setVisible(false));
            downloadLane.setStyle("-fx-background-color:  #B8D0FF; -fx-background-radius: 20;");
        }
        fade.play();
    }

    private void setDownloadLane() {
        //if есть что загружать
        ProgressBar load = new ProgressBar();
        load.setPrefSize(317, 14);
        load.setProgress(-1.0); //это имитация, надо прикрутить нормальную загрузку
        load.setStyle("-fx-control-inner-background: #B8D0FF; -fx-accent: #131F5A; -fx-background-radius: 6;" +
                "-fx-background-color: transparent; -fx-background-insets: 0; -fx-effect: none; -fx-padding: 0; " +
                "-fx-border-insets: 0;");
        load.setLayoutY(5);
        load.setLayoutX(14);

        Label status = new Label("Status");
        Font manropeFont2 = Font.loadFont(getClass().getResourceAsStream("/fonts/Manrope-Regular.ttf"), 16);
        status.setStyle("-fx-font-family: \"Manrope Regular\"; -fx-font-size: 12; -fx-text-fill: #131F5A;");
        status.setFont(manropeFont2);
        status.setLayoutX(38);
        status.setLayoutY(20);

        load.setOnMouseClicked(event -> toggleDownloads());

        downloadLane.getChildren().addAll(load, status);
    }

    private void setDownloadPane() {
        VBox contentContainer = new VBox();
        downloadScroll.setContent(contentContainer);

        downloadScroll.setPrefHeight(93);
        downloadScroll.setFitToWidth(true);

        for (int i = 0; i < 6; i++) {
            addItem(contentContainer);
        }

        configureScrollPane(contentContainer);
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

    private void configureScrollPane(VBox contentContainer) {
        contentContainer.heightProperty().removeListener(this::contentHeightChanged);
        contentContainer.heightProperty().addListener(this::contentHeightChanged);
        downloadScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        downloadScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    }


    private void contentHeightChanged(ObservableValue<? extends Number> obs, Number oldVal, Number newVal) {
        double newHeight = newVal.doubleValue();
        double maxBottomY = downloadPane.getLayoutY() + downloadPane.getPrefHeight();

        if (newHeight > MAX_HEIGHT_WITHOUT_SCROLL) {
            downloadScroll.setPrefHeight(MAX_HEIGHT_WITHOUT_SCROLL);
            downloadPane.setPrefHeight(MAX_HEIGHT_WITHOUT_SCROLL);
        } else {
            downloadScroll.setPrefHeight(newHeight);
            downloadPane.setPrefHeight(newHeight);
        }
        double newTopY = maxBottomY - downloadPane.getPrefHeight();
        if (newTopY < 590) {
            downloadPane.setLayoutY(newTopY);
        }
    }

    private void addItem(VBox container) {
        Pane item = new Pane();
        item.setStyle("-fx-background-color: #3A5CB9; -fx-background-radius: 16;");
        item.setPrefSize(250,74);

        Label name = new Label("Name");
        Font manropeFont = Font.loadFont(getClass().getResourceAsStream("/fonts/Manrope-Medium.ttf"), 16);
        name.setStyle("-fx-font-family: \"Manrope Medium\";-fx-font-size: 14; -fx-text-fill: white;");
        name.setFont(manropeFont);
        name.setLayoutX(49);
        name.setLayoutY(5);

        ProgressBar load = new ProgressBar();
        load.setPrefSize(200, 14);
        load.setProgress(-1.0);
        load.setStyle("-fx-control-inner-background: #B8D0FF; -fx-accent: #131F5A; -fx-background-radius: 6;" +
                "-fx-background-color: transparent; -fx-background-insets: 0; -fx-effect: null;");
        load.setLayoutY(30);
        load.setLayoutX(49);
        downloadScroll.getStyleClass().add("download-scroll-pane");

        Label status = new Label("В ожидании обработки");
        Font manropeFont2 = Font.loadFont(getClass().getResourceAsStream("/fonts/Manrope-Regular.ttf"), 16);
        status.setStyle("-fx-font-family: \"Manrope Regular\"; -fx-font-size: 12; -fx-text-fill: #131F5A;");
        status.setFont(manropeFont2);
        status.setLayoutX(49);
        status.setLayoutY(50);

        ImageView im = new ImageView();
        im.setImage(new Image("/images/Group9.png"));
        im.setFitHeight(38);
        im.setFitWidth(28);
        im.setLayoutX(8);
        im.setLayoutY(15);

        ImageView close = new ImageView();
        close.setImage(new Image("/images/CloseCircle4.png"));
        close.setFitHeight(27);
        close.setFitWidth(27);
        close.setLayoutX(265);
        close.setLayoutY(25);

        VBox.setMargin(item, new javafx.geometry.Insets(7, 10, 7, 7));
        item.getChildren().addAll(name, im, load, close, status);
        container.getChildren().add(item);
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
