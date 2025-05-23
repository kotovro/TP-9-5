package ui.custom_elements;

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
import logic.text_edit.EditStory;
import logic.text_edit.action.AddReplica;
import logic.text_edit.action.RemoveReplicas;
import logic.text_edit.action.StoryPoint;
import ui.custom_elements.combo_boxes.SearchableComboBox;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseDisplayer implements EditableDisplayer {
    private static final String STYLE = TranscriptDisplayer.class.getResource("/styles/style.css").toExternalForm();
    protected final List<Speaker> speakers;
    protected VBox textAreaContainer = new VBox();
    protected final Insets basePaneInsets = new Insets(10, 50, 0, 50);
    private final EditStory editStory = new EditStory();
    private EventHandler<KeyEvent> keyEventHandler;
    private final String name;

    public BaseDisplayer(String name, List<Speaker> speakers) {
        this.name = name;
        this.speakers = speakers;
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

    protected TextArea initTextArea(String text) {
        TextArea textArea = new CustomTextArea();
        textArea.setText(text);

        VBox.setMargin(textArea, new javafx.geometry.Insets(0, 0, 15, 0));
        textArea.setEditable(true);
        return textArea;
    }

    protected void addNewReplica(int index) {
        Replica replica = new Replica("", speakers.getFirst());
        StoryPoint storyPoint = new AddReplica(textAreaContainer, formReplicaView(replica), index);
        storyPoint.apply();
        editStory.addLast(storyPoint);
    }

    protected BasePane formReplicaView(Replica replica) {
        ComboBox<Speaker> comboBox = new SearchableComboBox(speakers, replica.getSpeaker());
        TextArea textArea = initTextArea(replica.getText());
        ImageView deleteButton = new ImageView();
        BasePane basepane = new BasePane(comboBox, textArea, deleteButton);
        VBox.setMargin(basepane, basePaneInsets);
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

    public abstract void saveToDB();
    protected abstract void initTextArea();
}
