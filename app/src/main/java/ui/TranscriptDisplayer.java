package ui;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import logic.general.Replica;
import logic.general.Speaker;
import logic.general.Transcript;
import logic.text_edit.EditStory;
import logic.text_edit.action.AddReplica;
import logic.text_edit.action.RemoveReplicas;
import logic.text_edit.action.StoryPoint;
import ui.custom_elements.CustomComboBox;
import ui.custom_elements.CustomTextArea;
import ui.custom_elements.BasePane;

import java.util.ArrayList;
import java.util.List;

public class TranscriptDisplayer {
    private static final String STYLE = TranscriptDisplayer.class.getResource("/styles/style.css").toExternalForm();
    private final Transcript transcript;
    private final List<Speaker> speakers;
    VBox textAreaContainer = new VBox();
    private final EditStory editStory = new EditStory();
    private final EventHandler<KeyEvent> keyEventHandler;

    public TranscriptDisplayer(Transcript transcript, List<Speaker> speakers) {
        this.transcript = transcript;
        this.speakers = speakers;

        if (textAreaContainer.getScene() != null) {
            textAreaContainer.getScene().getStylesheets().add(STYLE);
        } else {
            textAreaContainer.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    newScene.getStylesheets().add(STYLE);
                }
            });
        }
        for (Replica replica : transcript.getReplicas()) {
            textAreaContainer.getChildren().add(formReplicaView(replica));
        }

        this.keyEventHandler = event -> {
            if (event.getCode() == KeyCode.DELETE) {
                System.out.println("Delete pressed");
                StoryPoint storyPoint = new RemoveReplicas(textAreaContainer, getToRemove());
                storyPoint.apply();
                editStory.addLast(storyPoint);
            } else if (event.getCode() == KeyCode.N && event.isControlDown()) {
                addNewReplica(0);
            }
        };
    }

    public void setupPane(ScrollPane replicas) {
        replicas.setContent(textAreaContainer);
    }

    public void setupHotkeys() {
        textAreaContainer.getScene().addEventFilter(KeyEvent.KEY_PRESSED, keyEventHandler);
    }

    public void unbindHotKeys() {
        textAreaContainer.getScene().removeEventFilter(KeyEvent.KEY_PRESSED, keyEventHandler);
    }

    public String getName() {
        return transcript.getName();
    }

    private TextArea initTextArea(Replica replica, ComboBox<Speaker> comboBox) {
        TextArea textArea = new CustomTextArea(replica);
        textArea.setText(replica.getText());

        VBox.setMargin(textArea, new javafx.geometry.Insets(0, 0, 15, 0));
        textArea.setEditable(true);
        return textArea;
    }

    private void addNewReplica(int index) {
        Replica replica = new Replica("", speakers.getFirst());
        StoryPoint storyPoint = new AddReplica(textAreaContainer, formReplicaView(replica), index);
        storyPoint.apply();
        editStory.addLast(storyPoint);
    }

    private BasePane formReplicaView(Replica replica) {
        ComboBox<Speaker> comboBox = new CustomComboBox(speakers, replica.getSpeaker());
        TextArea textArea = initTextArea(replica, comboBox);
        ImageView deleteButton = new ImageView();
        BasePane basepane = new BasePane(comboBox, textArea, deleteButton);
        VBox.setMargin(basepane, new Insets(10, 50, 0, 50));
        return basepane;
    }

    private List<Integer> getToRemove() {
        List<Integer> toRemove = new ArrayList<>();
        int index = 0;
        for (Node node : textAreaContainer.getChildren()) {
            BasePane basePane = (BasePane) node;
            if (basePane.isSelected()) {
                toRemove.add(index);
            }
            index++;
        }
        return toRemove;
    }
}
