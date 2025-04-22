package ui;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import logic.text_edit.EditStory;
import ui.hotkeys.HotkeysPressedActionProvider;

import java.util.List;

public class EditController {
    public EditStory editStory = new EditStory();

    @FXML
    private AnchorPane rootPane = new AnchorPane();

    @FXML
    private VBox textAreaContainer;

    @FXML
    public void initialize() {
        List<Speaker> speakers = List.of(
                new Speaker("Anna", "/images/logo.png"),
                new Speaker("Boris", "/images/UserSpeak.png"),
                new Speaker("Viktor", "/images/UserSpeak2.png"),
                new Speaker("Galina", "/images/DangerCircle.png")
        );

        int replicas = 5;
        for (int i = 0; i < replicas; i++) {
            ComboBox<Speaker> comboBox = new ComboBox<>();
            comboBox.getItems().addAll(speakers);
            comboBox.setPrefWidth(160);
            comboBox.setPrefHeight(32);
            comboBox.getStyleClass().add("custom-combobox");

            comboBox.setCellFactory(lv -> new ListCell<>() {
                private final ImageView imageView = new ImageView();

                {
                    imageView.setFitHeight(20);
                    imageView.setFitWidth(20);
                    javafx.scene.shape.Circle clip = new javafx.scene.shape.Circle(10, 10, 10);
                    imageView.setClip(clip);
                }

                @Override
                protected void updateItem(Speaker item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        imageView.setImage(new Image(getClass().getResourceAsStream(item.getImagePath())));
                        setGraphic(imageView);
                        setText(item.getName());
                        setStyle("-fx-background-color: #9DA0FA; -fx-text-fill: white; -fx-border-color: #6366B5; -fx-border-width: 0 0 1px 0;");
                    }
                }
            });

            comboBox.setButtonCell(new ListCell<>() {
                private final ImageView imageView = new ImageView();

                {
                    imageView.setFitHeight(20);
                    imageView.setFitWidth(20);
                    javafx.scene.shape.Circle clip = new javafx.scene.shape.Circle(10, 10, 10);
                    imageView.setClip(clip);
                }

                @Override
                protected void updateItem(Speaker item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        imageView.setImage(new Image(getClass().getResourceAsStream(item.getImagePath())));
                        setGraphic(imageView);
                        setText(item.getName());
                        setStyle("-fx-text-fill: white;");
                    }
                }
            });

            VBox.setMargin(comboBox, new javafx.geometry.Insets(0, 0, 5, 0));
            textAreaContainer.getChildren().add(comboBox);

            TextArea textArea = new TextArea();
            textArea.setWrapText(true);
            textArea.setPrefRowCount(1);
            textArea.setMinHeight(Region.USE_PREF_SIZE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            textArea.setPrefHeight(Region.USE_COMPUTED_SIZE);
            textArea.getStyleClass().add("custom-text-area");

            Text helper = new Text();
            helper.setFont(textArea.getFont());
            helper.setWrappingWidth(textArea.getWidth() - 20);
            helper.setText(textArea.getText());

            textArea.widthProperty().addListener((obs, oldWidth, newWidth) -> {
                helper.setWrappingWidth(newWidth.doubleValue() - 20);
                helper.setText(textArea.getText() + "\n ");
                textArea.setPrefHeight(helper.getLayoutBounds().getHeight() + 20);
            });

            textArea.textProperty().addListener((obs, oldText, newText) -> {
                helper.setText(newText + "\n ");
                double height = helper.getLayoutBounds().getHeight() + 20;
                textArea.setPrefHeight(height);
            });

            VBox.setMargin(textArea, new javafx.geometry.Insets(0, 0, 20, 0));
            textAreaContainer.getChildren().add(textArea);
        }

        Button saveButton = new Button("сохранить");
        saveButton.setText("\u0421\u043E\u0445\u0440\u0430\u043D\u0438\u0442\u044C");
        saveButton.setStyle("""
    -fx-background-color: #3338D5;
    -fx-text-fill: white;
    -fx-font-family: "Arial";
    -fx-font-size: 14px;
    -fx-background-radius: 12px;
    -fx-padding: 6 16;
        """);
        saveButton.setOnAction(event -> {
            // то, что вам нужно
        });

        VBox.setMargin(saveButton, new javafx.geometry.Insets(15, 0, 0, 0));
        textAreaContainer.getChildren().add(saveButton);
        textAreaContainer.getStyleClass().add("vbox-transparent");


    }
}
