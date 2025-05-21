package ui;

import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import logic.general.Replica;
import logic.general.Speaker;
import logic.general.Transcript;
import logic.persistence.DBManager;
import logic.text_edit.EditStory;
import logic.text_edit.action.AddReplica;
import logic.text_edit.action.RemoveReplica;
import logic.text_edit.action.StoryPoint;
import ui.custom_elements.CustomComboBox;
import ui.custom_elements.CustomTextArea;
import ui.main_panes.BasePane;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Stenogramm {
    private Transcript transcript;
    private List<Speaker> speakers = new ArrayList<>();
    VBox textAreaContainer = new VBox();
    private CustomTextArea activeTextArea = null;
    private final EditStory editStory = new EditStory();
    private boolean alreadyLoaded = false;

    public Stenogramm(Transcript transcript) {
        this.transcript = transcript;
    }

    public ScrollPane fillPane(ScrollPane replicas) {
        if (alreadyLoaded) return replicas;
        speakers = DBManager.getSpeakerDao().getAllSpeakers();
        if (replicas.getScene() != null) {
            replicas.getScene().getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("/styles/style.css")).toExternalForm()
            );
        } else {
            replicas.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    newScene.getStylesheets().add(
                            Objects.requireNonNull(getClass().getResource("/styles/style.css")).toExternalForm()
                    );
                }
            });
        }
        for (Replica replica : transcript.getReplicas()) {
            ComboBox<Speaker> comboBox = new CustomComboBox(speakers, replica.getSpeaker());
            TextArea textArea = initTextArea(replica, comboBox);
            ImageView deleteButton = new ImageView();
            BasePane basepane = new BasePane(comboBox, textArea, deleteButton);
            textAreaContainer.setMargin(basepane, new Insets(10, 50, 0, 50));
            textAreaContainer.getChildren().add(basepane);
        }
        replicas.setContent(textAreaContainer);
        alreadyLoaded = true;
        return replicas;
    }

    public String getName() {
        return transcript.getName();
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

    private void addNewReplica() {
        Replica replica = new Replica("", speakers.getFirst());
        ComboBox<Speaker> comboBox = new CustomComboBox(speakers, speakers.getFirst());
        TextArea textArea = initTextArea(replica, comboBox);
        int index = textAreaContainer.getChildren().indexOf(activeTextArea) + 1;
        StoryPoint storyPoint = new AddReplica(textAreaContainer, comboBox, textArea, index);
        storyPoint.apply();
        editStory.addLast(storyPoint);
    }
}
