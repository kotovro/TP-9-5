package ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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

public class EditController {
    private Transcript transcript = new Transcript("", new Date());
    private List<Speaker> speakers = new ArrayList<>();

    private final EditStory editStory = new EditStory();
    private CustomTextArea activeTextArea = null;

    @FXML
    private AnchorPane rootPane = new AnchorPane();

    @FXML
    private VBox textAreaContainer;

    @FXML
    public void initialize() {
        speakers = List.of(
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
            ComboBox<Speaker> comboBox = initComboBox();
            textAreaContainer.getChildren().add(comboBox);
            TextArea textArea = initTextArea(replica, comboBox);
            textAreaContainer.getChildren().add(textArea);
        }

        textAreaContainer.getChildren().add(createSaveButton());
        textAreaContainer.getStyleClass().add("vbox-transparent");
    }

    private ComboBox<Speaker> initComboBox() {
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
        return comboBox;
    }

    private TextArea initTextArea(Replica replica, ComboBox<Speaker> comboBox) {
        TextArea textArea = formTextArea(replica, comboBox);
        textArea.setText(replica.getText().toString());

        VBox.setMargin(textArea, new javafx.geometry.Insets(0, 0, 15, 0));
        textArea.setEditable(false);
        return textArea;
    }

    private TextArea formTextArea(Replica replica, ComboBox<Speaker> comboBox) {
        CustomTextArea textArea = new CustomTextArea(replica);
        textArea.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (activeTextArea != null && activeTextArea != textArea) activeTextArea.setEditable(false);
            activeTextArea = textArea;
            if (event.getClickCount() == 2) {
                textArea.setEditable(true);
            }
        });

        textArea.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                activeTextArea.setEditable(false);
                activeTextArea = null;
            }
        });

        textArea.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DELETE && !textArea.isEditable()) {
                transcript.removeReplica(replica);
                textAreaContainer.getChildren().remove(comboBox);
                textAreaContainer.getChildren().remove(textArea);
            } else if (event.getCode() == KeyCode.N && event.isControlDown()) {
                addNewReplica();
            }
        });
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

    private void addNewReplica() {
        Replica replica = new Replica("", speakers.getFirst());
        transcript.addReplicaAfter(replica, activeTextArea.getReplica());
        ComboBox<Speaker> comboBox = initComboBox();
        TextArea textArea = initTextArea(replica, comboBox);
        int index = textAreaContainer.getChildren().indexOf(activeTextArea) + 1;
        textAreaContainer.getChildren().add(index, textArea);
        textAreaContainer.getChildren().add(index, comboBox);
    }

    private static Image getImage(String path) {
        return new Image(Objects.requireNonNull(EditController.class.getResourceAsStream(path)));
    }
}
