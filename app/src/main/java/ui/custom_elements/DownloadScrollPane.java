package ui.custom_elements;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import logic.video_processing.queue.listeners.QueueChangeListener;
import ui.BaseController;

import java.util.ArrayList;
import java.util.List;

public class DownloadScrollPane extends ScrollPane implements QueueChangeListener {
    private static final double MAX_HEIGHT_WITHOUT_SCROLL = 332;
//    private static final Image closeCircleImage = new Image(BaseController.class.getResource("/images/CloseCircle4.png").toExternalForm());
    private static final Image groupImage = new Image(BaseController.class.getResource("/images/Group9.png").toExternalForm());
    private static final Font manropeFont2 = Font.loadFont(DownloadScrollPane.class.getResourceAsStream("/fonts/Manrope-Regular.ttf"), 16);
    private static final Font manropeFont = Font.loadFont(DownloadScrollPane.class.getResourceAsStream("/fonts/Manrope-Medium.ttf"), 16);
    private final VBox contentContainer = new VBox();
    private final BaseController baseController;

    public DownloadScrollPane(BaseController baseController) {
        this.baseController = baseController;
        setPrefHeight(93);
        setFitToWidth(true);

        setMaxHeight(332.0);
        setMaxWidth(334.0);
        setMinHeight(94.0);
        setMinWidth(334.0);
        setPrefHeight(94.0);
        setPrefWidth(334.0);

        setStyle("-fx-background-color: #0A2A85; -fx-background-radius: 20;");
        setContent(contentContainer);
        configureScrollPane(contentContainer);
    }

    @Override
    public void onQueueChange(List<String> taskQueue) {
        Platform.runLater(() -> {
            updateDownloadPane(taskQueue);
        });
    }

    private void updateDownloadPane(List<String> taskNames) {
        setVisible(false);
        contentContainer.getChildren().clear();
        for (String taskName : taskNames.reversed()) {
            addItem(taskName);
        }
        setVisible(!taskNames.isEmpty());
    }

    private void configureScrollPane(VBox contentContainer) {
        contentContainer.heightProperty().addListener(this::contentHeightChanged);
        setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    }


    private void contentHeightChanged(ObservableValue<? extends Number> obs, Number oldVal, Number newVal) {
        double newHeight = newVal.doubleValue();
        double maxBottomY = getLayoutY() + getPrefHeight();

        double height = Math.min(newHeight, MAX_HEIGHT_WITHOUT_SCROLL);
        setPrefHeight(height);
        double newTopY = maxBottomY - height;
        setLayoutY(newTopY);

        contentContainer.requestLayout();
        this.requestLayout();
        forceFullUpdate();
    }

    private void forceFullUpdate() {
        Pane temp = new Pane();
        Platform.runLater(() -> {
            setContent(temp);
            Platform.runLater(() -> {
                setContent(contentContainer);
                contentContainer.requestLayout();
            });
        });
    }

    private void addItem(String taskName) {
        Pane item = new Pane();
        item.setStyle("-fx-background-color: #3A5CB9; -fx-background-radius: 16;");
        item.setPrefSize(250,74);

        Label name = new Label(taskName);
        name.setStyle("-fx-font-family: \"Manrope Medium\";-fx-font-size: 14; -fx-text-fill: white;");
        name.setFont(manropeFont);
        name.setLayoutX(49);
        name.setLayoutY(5);

        ProgressBar load = new ProgressBar();
        load.setPrefSize(200, 14);
        load.setProgress(0);
        load.setStyle("-fx-control-inner-background: #B8D0FF; -fx-accent: #131F5A; -fx-background-radius: 6;" +
                "-fx-background-color: transparent; -fx-background-insets: 0; -fx-effect: null;");
        load.setLayoutY(30);
        load.setLayoutX(49);
        getStyleClass().add("download-scroll-pane");

        Label status = new Label("В ожидании обработки");
        status.setStyle("-fx-font-family: \"Manrope Regular\"; -fx-font-size: 12; -fx-text-fill: #ffffff;");
        status.setFont(manropeFont2);
        status.setLayoutX(49);
        status.setLayoutY(50);

        ImageView im = new ImageView();
        im.setImage(groupImage);
        im.setFitHeight(38);
        im.setFitWidth(28);
        im.setLayoutX(8);
        im.setLayoutY(15);

//        ImageView close = new ImageView();
//        close.setImage(closeCircleImage);
//        close.setFitHeight(27);
//        close.setFitWidth(27);
//        close.setLayoutX(265);
//        close.setLayoutY(25);

        VBox.setMargin(item, new javafx.geometry.Insets(7, 10, 7, 7));
        item.getChildren().addAll(name, im, load, status);
        contentContainer.getChildren().add(item);

        if (!baseController.isDownloadsOpen()) {
            baseController.toggleDownloads();
        }
    }

    public boolean isEmpty() {
        return contentContainer.getChildren().isEmpty();
    }
}
