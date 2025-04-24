package ui;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javafx.stage.Stage;
import javafx.util.Pair;
import logic.general.Speaker;
import logic.general.Replica;
import javafx.scene.layout.AnchorPane;
import logic.general.Transcript;
import logic.text_edit.EditStory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import logic.text_edit.EditStory;
import logic.text_edit.action.AddReplica;
import logic.text_edit.action.RemoveReplica;
import logic.text_edit.action.StoryPoint;
import ui.custom_elements.CustomComboBox;
import ui.custom_elements.CustomTextArea;

public class EditController {
    private Transcript transcript;
    private List<Speaker> speakers = new ArrayList<>();

    private final EditStory editStory = new EditStory();
    private CustomTextArea activeTextArea = null;

    @FXML
    private AnchorPane rootPane = new AnchorPane();

    @FXML
    private VBox textAreaContainer;

    @FXML
    private Button saveButton;

    @FXML
    private Button loadButton;

    public void setTranscript(Transcript transcript) {
        this.transcript = transcript;
    }

    @FXML
    public void initialize() {
        speakers = List.of(
                new Speaker("Anna", getImage("/images/logo.png"), 1),
                new Speaker("Boris", getImage("/images/UserSpeak.png"), 2),
                new Speaker("Viktor", getImage("/images/UserSpeak2.png"), 3),
                new Speaker("Galina", getImage("/images/DangerCircle.png"), 4)
        );

        transcript.addReplica(new Replica("meme", speakers.getFirst()));
        transcript.addReplica(new Replica("meme", speakers.getFirst()));
        transcript.addReplica(new Replica("meme", speakers.getFirst()));
        transcript.addReplica(new Replica("meme", speakers.getFirst()));
        transcript.addReplica(new Replica("meme", speakers.getFirst()));

        for (Replica replica : transcript.getReplicas()) {
            ComboBox<Speaker> comboBox = new CustomComboBox(speakers);
            textAreaContainer.getChildren().add(comboBox);
            TextArea textArea = initTextArea(replica, comboBox);
            textAreaContainer.getChildren().add(textArea);
        }

        rootPane.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (activeTextArea != null) return;
            if (event.getCode() == KeyCode.Z && event.isControlDown()) {
                if (editStory.canUndo()) editStory.undoLast();
                event.consume();
            } else if (event.getCode() == KeyCode.Y && event.isControlDown()) {
                if (editStory.canRedo()) editStory.redoLast();
                event.consume();
            }
        });

        textAreaContainer.getChildren().add(createSaveButton());
        textAreaContainer.getStyleClass().add("vbox-transparent");
    }

    private TextArea initTextArea(Replica replica, ComboBox<Speaker> comboBox) {
        TextArea textArea = formTextArea(replica, comboBox);
        textArea.setText(replica.getText());

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
                textArea.setEditable(false);
                activeTextArea = null;
            }
        });

        textArea.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DELETE && !textArea.isEditable()) {
                int index = textAreaContainer.getChildren().indexOf(comboBox);
                textAreaContainer.getChildren().remove(comboBox);
                textAreaContainer.getChildren().remove(textArea);
                editStory.addLast(new RemoveReplica(textAreaContainer, comboBox, textArea, index));
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

        loadButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx_screens/LoadOptional.fxml"));
                Scene scene = new Scene(loader.load());
                scene.getStylesheets().add(getClass().getResource("/styles/dialog-style.css").toExternalForm());

                Stage dialog = new Stage();
                dialog.initOwner(loadButton.getScene().getWindow());
                dialog.setTitle("Выбор источника загрузки");
                dialog.setScene(scene);
                dialog.setResizable(false);
                dialog.showAndWait(); // или dialog.showAndWait();

                Stage currentStage = (Stage) loadButton.getScene().getWindow();
                currentStage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        textAreaContainer.getStyleClass().add("vbox-transparent");
        VBox.setMargin(saveButton, new javafx.geometry.Insets(15, 0, 0, 0));
        return saveButton;
    }

    private void addNewReplica() {
        Replica replica = new Replica("", speakers.getFirst());
        ComboBox<Speaker> comboBox = new CustomComboBox(speakers);
        TextArea textArea = initTextArea(replica, comboBox);
        int index = textAreaContainer.getChildren().indexOf(activeTextArea) + 1;
        StoryPoint storyPoint = new AddReplica(textAreaContainer, comboBox, textArea, index);
        storyPoint.apply();
        editStory.addLast(storyPoint);
    }

    private static Image getImage(String path) {
        return new Image(Objects.requireNonNull(EditController.class.getResourceAsStream(path)));
    }
}
