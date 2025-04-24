package ui;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import logic.general.Speaker;
import logic.general.Replica;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import logic.general.Transcript;
import logic.text_edit.EditStory;
import logic.text_edit.action.AddText;
import logic.text_edit.action.RemoveText;
import logic.text_edit.action.ReplaceText;

public class EditController {
    Replica currentReplica = null;
    private Transcript transcript = new Transcript("", new Date());
    private final EditStory editStory = new EditStory();

    public static TextEditUnit getDifferenceWithIndex(String oldText, String newText) {
        int minLength = Math.min(oldText.length(), newText.length());
        int diffStart = 0;

        // Find start of difference
        while (diffStart < minLength && oldText.charAt(diffStart) == newText.charAt(diffStart)) {
            diffStart++;
        }

        // Find end of difference
        int diffEndOld = oldText.length() - 1;
        int diffEndNew = newText.length() - 1;
        while (diffEndOld >= diffStart && diffEndNew >= diffStart &&
                oldText.charAt(diffEndOld) == newText.charAt(diffEndNew)) {
            diffEndOld--;
            diffEndNew--;
        }

        String removed = oldText.substring(diffStart, diffEndOld + 1);
        String added = newText.substring(diffStart, diffEndNew + 1);

        return new TextEditUnit(diffStart, added, removed);
    }

    @FXML
    private AnchorPane rootPane = new AnchorPane();

    @FXML
    private VBox textAreaContainer;

    @FXML
    public void initialize() {
        List<Speaker> speakers = List.of(
                new Speaker("Anna", getImage("/images/logo.png")),
                new Speaker("Boris", getImage("/images/UserSpeak.png")),
                new Speaker("Viktor", getImage("/images/UserSpeak2.png")),
                new Speaker("Galina", getImage("/images/DangerCircle.png"))
        );

        transcript.addReplica(new Replica("meme", speakers.getFirst()));
        transcript.addReplica(new Replica("meme", speakers.getFirst()));
        transcript.addReplica(new Replica("meme", speakers.getFirst()));
        transcript.addReplica(new Replica("meme", speakers.getFirst()));
        transcript.addReplica(new Replica("meme", speakers.getFirst()));

        for (Replica replica : transcript.getReplicas()) {
            initComboBox(speakers);
            initTextArea(replica);
        }

        textAreaContainer.getChildren().add(createSaveButton());
        textAreaContainer.getStyleClass().add("vbox-transparent");
    }

    private void initComboBox(List<Speaker> speakers) {
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
            protected void updateItem(Speaker speaker, boolean empty) {
                super.updateItem(speaker, empty);
                if (empty || speaker == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    imageView.setImage(speaker.getImage());
                    setGraphic(imageView);
                    setText(speaker.getName());
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
            protected void updateItem(Speaker speaker, boolean empty) {
                super.updateItem(speaker, empty);
                if (empty || speaker == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    imageView.setImage(speaker.getImage());
                    setGraphic(imageView);
                    setText(speaker.getName());
                    setStyle("-fx-text-fill: white;");
                }
            }
        });

        VBox.setMargin(comboBox, new javafx.geometry.Insets(0, 0, 5, 0));
        textAreaContainer.getChildren().add(comboBox);
    }

    private void initTextArea(Replica replica) {
        TextArea textArea = formTextArea();
        VBox.setMargin(textArea, new javafx.geometry.Insets(0, 0, 20, 0));
        textArea.setText(replica.getText().toString());

        VBox.setMargin(textArea, new javafx.geometry.Insets(0, 0, 50, 0));
        textAreaContainer.getChildren().add(textArea);
    }

    private TextArea formTextArea() {
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
        textArea.setPrefRowCount(3);
        return textArea;
    }

    private static Button createSaveButton() {
        Button saveButton = new Button("сохранить");
        saveButton.setText("Сохранить");
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
        return saveButton;
    }

    private static Image getImage(String path) {
        return new Image(Objects.requireNonNull(EditController.class.getResourceAsStream(path)));
    }

    }
}
