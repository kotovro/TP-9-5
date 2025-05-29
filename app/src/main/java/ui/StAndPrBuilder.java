package ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.image.ImageView;
import logic.general.MeetingMaterials;
import logic.video_processing.queue.ProcessingQueue;
import logic.video_processing.queue.listeners.SummarizeListener;

import java.text.SimpleDateFormat;

public class StAndPrBuilder extends HBox {
    private static final Font manropeFont = Font.loadFont(StAndPrBuilder.class.getResourceAsStream("/fonts/Manrope-ExtraBold.ttf"), 16);
    private static final Font manropeFont2 = Font.loadFont(StAndPrBuilder.class.getResourceAsStream("/fonts/Manrope-Bold.ttf"), 16);
    private static final Font manropeFont3 = Font.loadFont(StAndPrBuilder.class.getResourceAsStream("/fonts/Manrope-Regular.ttf"), 16);
    private static final Image ARROW = new Image(StAndPrBuilder.class.getResource("/images/SquareAltArrowRight.png").toString());
    private Pane transcriptPane = new Pane();
    private Button protocolButton = new Button();

    private MeetingMaterials meetingMaterials;
    private EditWindowController editWindowController;
    private BaseController baseController;

    public StAndPrBuilder(MeetingMaterials meetingMaterials, EditWindowController editWindowController,
                          ProcessingQueue processingQueue, BaseController baseController, SavesController savesController) {
        this.meetingMaterials = meetingMaterials;
        this.editWindowController = editWindowController;
        this.baseController = baseController;

        Label name = new Label(meetingMaterials.getTranscript().getName());
        name.setStyle("-fx-font-family: \"Manrope ExtraBold\"; -fx-font-size: 18; -fx-text-fill: black;");
        name.setFont(manropeFont);
        name.setLayoutY(24);
        name.setLayoutX(18);

        Pane datePane = new Pane();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = dateFormat.format(meetingMaterials.getTranscript().getDate());
        Label date = new Label(formattedDate);
        date.setStyle("-fx-font-family: \"Manrope Regular\"; -fx-font-size: 14; -fx-text-fill: black;");
        date.setFont(manropeFont3);
        date.setLayoutY(10);
        date.setLayoutX(10);

        datePane.setLayoutY(76);
        datePane.setLayoutX(578);
        datePane.setPrefSize(92, 37);
        datePane.setStyle("-fx-background-color: #E7EFFF; -fx-background-radius: 8;");
        datePane.getChildren().add(date);

        int size = 37;
        ImageView imageView = new ImageView(ARROW);
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
        imageView.setPreserveRatio(false);

        Button arrowButton = new Button();
        arrowButton.setGraphic(imageView);
        arrowButton.setStyle("-fx-background-color: transparent; " + "-fx-padding: 0; " + "-fx-border-width: 0;");
        arrowButton.setPrefSize(size, size);
        arrowButton.setMinSize(size, size);
        arrowButton.setMaxSize(size, size);
        arrowButton.requestLayout();
        arrowButton.setLayoutX(613);
        arrowButton.setLayoutY(28);
        arrowButton.setShape(new Rectangle(size, size));
        arrowButton.setOnAction(event -> {
            editWindowController.addTranscript(meetingMaterials.getTranscript());
            Platform.runLater(baseController::switchToEditPane);
        });

        transcriptPane.setPrefSize(684, 123);
        transcriptPane.setLayoutY(27);
        transcriptPane.setLayoutX(21);
        transcriptPane.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 20; -fx-border-color: #2A55D5; -fx-border-width: 3; " +
                "-fx-border-radius: 20;");
        transcriptPane.getChildren().addAll(name, datePane, arrowButton);

        protocolButton.setFont(manropeFont2);
        if (meetingMaterials.getProtocol().isPresent()) {
            protocolButton.setText("Открыть\nпротокол");
            protocolButton.setOnAction(e -> {
                editWindowController.addProtocol(meetingMaterials);
                Platform.runLater(baseController::switchToEditPane);
            });
        } else {
            protocolButton.setText("Создать\nпротокол");
            protocolButton.setOnAction(e -> {
                processingQueue.add(meetingMaterials.getTranscript());
                savesController.addToProcessing(meetingMaterials);
                protocolButton.setDisable(true);
            });
            protocolButton.setDisable(savesController.isProcessing(meetingMaterials));
        }
        protocolButton.setStyle("-fx-background-color: #2A55D5; -fx-background-radius: 10; -fx-text-fill: white;");
        protocolButton.setPrefSize(154, 50);
        setMargin(protocolButton,  new Insets(30, 0, 5, 20));
        this.getChildren().addAll(transcriptPane, protocolButton);
    }
}
