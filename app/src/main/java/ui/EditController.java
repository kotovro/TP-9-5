package ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javafx.stage.Stage;
import logic.general.Speaker;
import logic.general.Replica;
import javafx.scene.layout.AnchorPane;
import logic.general.Transcript;
import logic.persistence.DBManager;
import logic.text_edit.EditStory;
import logic.text_edit.action.AddReplica;
import logic.text_edit.action.RemoveReplica;
import logic.text_edit.action.StoryPoint;
import ui.custom_elements.CustomComboBox;
import ui.custom_elements.CustomTextArea;

public class EditController {
    private final Transcript transcript;
    private List<Speaker> speakers = new ArrayList<>();

    private final EditStory editStory = new EditStory();
    private CustomTextArea activeTextArea = null;

    public EditController(Transcript transcript) {
        this.transcript = transcript;
    }

    @FXML
    private AnchorPane rootPane = new AnchorPane();

    @FXML
    private VBox textAreaContainer;

    @FXML
    private Button saveButton;

    @FXML
    private Button loadButton;

    @FXML
    public void initialize() {
        speakers = DBManager.getSpeakerDao().getAllSpeakers();

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

        initButtons();
    }

    public Transcript formTranscript() {
        int index = 0;
        for (Transcript transcript : DBManager.getTranscriptDao().getTranscripts()) {
            index = Integer.parseInt(transcript.getName().substring(transcript.getName().length() - 1));
        }
        Transcript newTranscript = new Transcript("Транскрипция " + (index + 1), new Date());

        int i = 0;
        Speaker speaker = null;
        for (var container : textAreaContainer.getChildren()) {
            if (i % 2 == 0) {
                speaker = ((CustomComboBox)container).getSelectionModel().getSelectedItem();
            } else {
                String text = ((CustomTextArea)container).getText();
                newTranscript.addReplica(new Replica(text, speaker));
            }
            i++;
        }
        return newTranscript;
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

    private void initButtons() {
        saveButton.setOnAction(event -> {
            DBManager.getTranscriptDao().addTranscript(formTranscript());

            Stage stage = (Stage) saveButton.getScene().getWindow();
            LoadStenogrammApp.setStage(stage);
        });

        loadButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(EditController.class.getResource("/fx_screens/loadOptional.fxml"));
                Scene scene = new Scene(loader.load());
                scene.getStylesheets().add(getClass().getResource("/styles/dialog-style.css").toExternalForm());

                Stage dialog = new Stage();
                dialog.initOwner(loadButton.getScene().getWindow());
                dialog.setTitle("Выбор источника загрузки");
                dialog.setScene(scene);
                dialog.setResizable(false);

                LoadOptionDialogController controller = loader.getController();
                Stage mainStage = (Stage) loadButton.getScene().getWindow();
                controller.setMainStage(mainStage);

                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        textAreaContainer.getStyleClass().add("vbox-transparent");
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

    public static Image getImage(String path) {
        return new Image(Objects.requireNonNull(EditController.class.getResourceAsStream(path)));
    }
}
