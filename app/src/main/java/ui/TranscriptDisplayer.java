package ui;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
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
    public Button delete;
    public Button file;
    public Button edit;
    public boolean isOpen = false;
    public Pane filePane;

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
            BasePane bp = formReplicaView(replica);
            textAreaContainer.getChildren().add(bp);
            bp.cb.setOnAction(e -> updateDeleteButtonVisibility());
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
        replicas.getStyleClass().add("tab-scroll-pane");
        replicas.setContent(textAreaContainer);
    }

    public void setupDelete(Pane paneReplicas) {
        if (this.delete != null && paneReplicas.getChildren().contains(this.delete)) {
            paneReplicas.getChildren().remove(this.delete);
        }

        Button delete = new Button("удалить выбранные");
        delete.setLayoutY(-38);
        delete.setLayoutX(750);
        Font manropeFont2 = Font.loadFont(getClass().getResourceAsStream("/fonts/Manrope-Regular.ttf"), 16);
        delete.setStyle("-fx-background-color: #2A55D5; -fx-background-radius: 16; -fx-text-fill: white;" +
                " -fx-font-size: 14px; -fx-font-family: \"Manrope Regular\";");
        delete.setFont(manropeFont2);
        delete.setVisible(false); // Сначала скрыта

        delete.setOnAction(e -> {
            StoryPoint storyPoint = new RemoveReplicas(textAreaContainer, getToRemove());
            storyPoint.apply();
            editStory.addLast(storyPoint);
            updateDeleteButtonVisibility();
        });

        this.delete = delete;
        paneReplicas.getChildren().add(delete);

        for (Node node : textAreaContainer.getChildren()) {
            BasePane basePane = (BasePane) node;
            basePane.cb.setOnAction(e -> updateDeleteButtonVisibility());
        }
        updateDeleteButtonVisibility();
    }

    public void setupMenu(Pane paneReplicas) {
        ImageView im = new ImageView("/images/SquareAltArrowDown2.png");
        im.setFitHeight(18);
        im.setFitWidth(18);

        Button file = new Button("Файл", im);
        file.setLayoutY(-180);
        file.setLayoutX(230);
        Font manropeFont2 = Font.loadFont(getClass().getResourceAsStream("/fonts/Manrope-Medium.ttf"), 16);
        file.setStyle("-fx-background-color: #0A2A85; -fx-background-radius: 8; -fx-text-fill: white;" +
                " -fx-font-size: 16px; -fx-font-family: \"Manrope Medium\";");
        file.setFont(manropeFont2);
        file.setContentDisplay(ContentDisplay.RIGHT);

        Pane filePane = new Pane();
        filePane.setLayoutY(-140);
        filePane.setLayoutX(230);
        filePane.setPrefSize(200, 100);
        filePane.setStyle("-fx-background-color: #0A2A85; -fx-background-radius: 24;");
        filePane.setVisible(false);

        Button save = new Button("Save"); // исправь на русский, меня просто начало бесить, что странно все выводится
        Button saveAs = new Button("Save As");

        save.setLayoutX(15);
        save.setLayoutY(15);
        saveAs.setLayoutX(15);
        saveAs.setLayoutY(55);

        saveAs.setStyle("-fx-background-color: #2A55D5; -fx-background-radius: 16; -fx-text-fill: white;" +
                " -fx-font-size: 14px; -fx-font-family: \"Manrope Regular\";");
        saveAs.setFont(manropeFont2);

        save.setStyle("-fx-background-color: #2A55D5; -fx-background-radius: 16; -fx-text-fill: white;" +
                " -fx-font-size: 14px; -fx-font-family: \"Manrope Regular\";");
        save.setFont(manropeFont2);

        filePane.getChildren().addAll(save, saveAs);

        save.setOnAction(e -> {
            // как ты тут сохраняешь
        });

        saveAs.setOnAction(e -> {
            // вызов DialogSave
        });


        this.file = file;
        this.filePane = filePane;
        paneReplicas.getChildren().addAll(file, filePane);
    }

    private void updateDeleteButtonVisibility() {
        boolean anySelected = false;
        for (Node node : textAreaContainer.getChildren()) {
            BasePane basePane = (BasePane) node;
            if (basePane.cb.isSelected()) {
                anySelected = true;
                break;
            }
        }
        this.delete.setVisible(anySelected);
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
        ImageView addButton = new ImageView();
        CheckBox cb = new CheckBox();
        Pane timeCode = new Pane();
        BasePane basepane = new BasePane(comboBox, textArea, addButton, cb, timeCode);
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
