package ui;

import javafx.animation.TranslateTransition;
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
import javafx.util.Duration;
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
import ui.custom_elements.LinkedComboBox;
import ui.custom_elements.SyncedComboBoxManager;

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
    private Button saveAsButton;

    private final int MENU_WIDTH = 200;
    private boolean isMenuOpen = false;

    @FXML
    private VBox sideMenu;

    @FXML
    private Button menuButton;

    @FXML
    private Button main;

    @FXML
    private Button save;

    @FXML
    private Button change;


    private void toggleMenu() {
        isMenuOpen = !isMenuOpen;

        TranslateTransition animation = new TranslateTransition(Duration.millis(300), sideMenu);
        animation.setFromX(isMenuOpen ? -MENU_WIDTH : 0);
        animation.setToX(isMenuOpen ? 0 : -MENU_WIDTH);
        animation.play();
    }

    // Обработчики для пунктов меню
    @FXML
    private void handleMainClick() {
        GlobalState.transcript = formTranscript();
        Stage stage = (Stage) main.getScene().getWindow();
        MainWindow.setStage(stage);
    }

    @FXML
    private Button download;

    @FXML
    private void handleDownloadClick() {
        GlobalState.transcript = formTranscript();
        Stage stage = (Stage) download.getScene().getWindow();
        DownloadingApp.setStage(stage);
    }

    @FXML
    private void handleSavingsClick() {
        GlobalState.transcript = formTranscript();
        Stage stage = (Stage) save.getScene().getWindow();
        LoadStenogrammApp.setStage(stage);
    }

    @FXML
    private void handleEditClick() {

    }

    @FXML
    private void handleExitClick() {
        System.exit(0);
    }

    @FXML
    public void initialize() {
        menuButton.setOnAction(event -> toggleMenu());
        speakers = DBManager.getSpeakerDao().getAllSpeakers();

        for (Replica replica : transcript.getReplicas()) {
            ComboBox<Speaker> comboBox = new LinkedComboBox(speakers, replica.getSpeaker());
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
        Transcript newTranscript = new Transcript(transcript.getName(), new Date());

        int i = 0;
        Speaker speaker = null;
        for (var container : textAreaContainer.getChildren()) {
            if (i % 2 == 0) {
                speaker = ((LinkedComboBox)container).getSelectionModel().getSelectedItem();
            } else {
                String text = ((CustomTextArea)container).getText();
                newTranscript.addReplica(new Replica(text, speaker));
            }
            i++;
        }
        newTranscript.setId(GlobalState.transcript.getId());
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
            if (transcript.getId() < 0)
            {
                GlobalState.transcript = formTranscript();
                Saving.createDialog((Stage) saveAsButton.getScene().getWindow());
            } else {
                DBManager.getTranscriptDao().updateTranscript(formTranscript());
            }
        });

        saveAsButton.setOnAction(event -> {
            GlobalState.transcript = formTranscript();
            Saving.createDialog((Stage) saveAsButton.getScene().getWindow());
        });

        textAreaContainer.getStyleClass().add("vbox-transparent");
    }

    private void addNewReplica() {
        Replica replica = new Replica("", speakers.getFirst());
        SyncedComboBoxManager comboBoxManager = new SyncedComboBoxManager(speakers, speakers.getFirst());
        for (int i = 0; i < 3; i++) {
            ComboBox<Speaker> comboBox = comboBoxManager.createComboBox();
            TextArea textArea = initTextArea(replica, comboBox);
            int index = textAreaContainer.getChildren().indexOf(activeTextArea) + 1;
            StoryPoint storyPoint = new AddReplica(textAreaContainer, comboBox, textArea, index);
            storyPoint.apply();
            editStory.addLast(storyPoint);
        }
    }

    public static Image getImage(String path) {
        return new Image(Objects.requireNonNull(EditController.class.getResourceAsStream(path)));
    }
}
