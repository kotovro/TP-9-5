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
import logic.video_processing.vosk.analiseDTO.RawTranscript;
import ui.custom_elements.CustomTextArea;
import ui.custom_elements.BasePane;
import ui.custom_elements.combo_boxes.ComboBoxCreator;
import ui.custom_elements.combo_boxes.SearchableComboBox;

import java.util.ArrayList;
import java.util.List;

public class TranscriptDisplayer {
    private static final String PREFIX = "Без названия ";
    private static final String STYLE = TranscriptDisplayer.class.getResource("/styles/style.css").toExternalForm();
    private final List<Speaker> speakers;
    VBox textAreaContainer = new VBox();
    private final EditStory editStory = new EditStory();
    private EventHandler<KeyEvent> keyEventHandler;
    private final String name;
    private final Insets basePaneInsets = new Insets(10, 50, 0, 50);

    public TranscriptDisplayer(Transcript transcript, List<Speaker> speakers) {
        this.name = transcript.getName();
        this.speakers = speakers;

        for (Replica replica : transcript.getReplicas()) {
            textAreaContainer.getChildren().add(formReplicaView(replica));
        }
        init();
    }

    public TranscriptDisplayer(RawTranscript transcript, List<Speaker> speakers) {
        this.name = PREFIX + (transcript.getID() + 1);
        this.speakers = speakers;
        ComboBoxCreator comboBoxCreator = new ComboBoxCreator(transcript);
        for (int i = 0; i < transcript.getPhraseCount(); i++) {
            textAreaContainer.getChildren().add(formReplicaView(transcript, comboBoxCreator, i));
        }
        init();
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
        return name;
    }

    private void init() {
        if (textAreaContainer.getScene() != null) {
            textAreaContainer.getScene().getStylesheets().add(STYLE);
        } else {
            textAreaContainer.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    newScene.getStylesheets().add(STYLE);
                }
            });
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

    private TextArea initTextArea(String text) {
        TextArea textArea = new CustomTextArea();
        textArea.setText(text);

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
        ComboBox<Speaker> comboBox = new SearchableComboBox(speakers, replica.getSpeaker());
        TextArea textArea = initTextArea(replica.getText());
        ImageView deleteButton = new ImageView();
        BasePane basepane = new BasePane(comboBox, textArea, deleteButton);
        VBox.setMargin(basepane, basePaneInsets);
        return basepane;
    }

    private BasePane formReplicaView(RawTranscript rawTranscript, ComboBoxCreator comboBoxCreator, int index) {
        ComboBox<Speaker> comboBox = comboBoxCreator.createComboBox(index);
        TextArea textArea = initTextArea(rawTranscript.getPhrase(index));
        ImageView deleteButton = new ImageView();
        BasePane basePane = new BasePane(comboBox, textArea, deleteButton);
        VBox.setMargin(basePane, basePaneInsets);
        return basePane;
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
