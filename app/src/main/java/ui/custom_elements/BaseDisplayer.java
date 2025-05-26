package ui.custom_elements;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import logic.general.Replica;
import logic.general.Speaker;
import logic.text_edit.EditStory;
import logic.text_edit.action.AddReplica;
import logic.text_edit.action.RemoveReplicas;
import logic.text_edit.action.StoryPoint;
import logic.utils.TimeCode;
import logic.utils.TimeFormatter;
import ui.BaseController;
import ui.custom_elements.combo_boxes.SearchableComboBox;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseDisplayer implements EditableDisplayer {
    private static final String STYLE = TranscriptDisplayer.class.getResource("/styles/style.css").toExternalForm();
    private static final ImageView arrow = new ImageView(BaseDisplayer.class.getResource("/images/SquareAltArrowDown2.png").toExternalForm());
    private static final Font manropeFont1 = Font.loadFont(BaseDisplayer.class.getResourceAsStream("/fonts/Manrope-Regular.ttf"), 16);
    private static final Font manropeFont2 = Font.loadFont(BaseDisplayer.class.getResourceAsStream("/fonts/Manrope-Medium.ttf"), 16);
    protected final List<Speaker> speakers;
    protected VBox textAreaContainer = new VBox();
    protected final Insets basePaneInsets = new Insets(10, 50, 0, 50);
    private final EditStory editStory = new EditStory();
    private final String name;
    private EventHandler<KeyEvent> keyEventHandler;
    private Pane buttonsOverlay;

    protected Button delete;
    protected Button file;
    protected Pane filePane;
    protected Button save;
    protected Button saveAs;

    public BaseDisplayer(String name, List<Speaker> speakers) {
        this.name = name;
        this.speakers = speakers;
        init();
    }

    public void setupPane(ScrollPane replicas) {
        replicas.getStyleClass().add("tab-scroll-pane");
        replicas.setContent(textAreaContainer);
    }

    public void setupHotkeys() {
        if (textAreaContainer.getScene() != null) {
            textAreaContainer.getScene().addEventFilter(KeyEvent.KEY_PRESSED, keyEventHandler);
        } else {
            textAreaContainer.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    textAreaContainer.getScene().addEventFilter(KeyEvent.KEY_PRESSED, keyEventHandler);
                }
            });
        }
    }

    public void unbindHotKeys() {
        if (textAreaContainer.getScene() != null) {
            textAreaContainer.getScene().removeEventFilter(KeyEvent.KEY_PRESSED, keyEventHandler);
        } else {
            textAreaContainer.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    textAreaContainer.getScene().removeEventFilter(KeyEvent.KEY_PRESSED, keyEventHandler);
                }
            });
        }
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
                removeReplicas();
            } else if (event.getCode() == KeyCode.N && event.isControlDown()) {
                addNewReplica(0);
            }
        };

        initButtonsOverlay();
    }

    public void setupOverlay(Pane overlay) {
        overlay.getChildren().setAll(buttonsOverlay);
        updateDeleteButtonVisibility();
    }

    public void updateDeleteButtonVisibility() {
        boolean anySelected = false;
        for (Node node : textAreaContainer.getChildren()) {
            BasePane basePane = (BasePane) node;
            if (basePane.isSelected()) {
                anySelected = true;
                break;
            }
        }
        this.delete.setVisible(anySelected);
    }

    protected TextArea initTextArea(String text) {
        TextArea textArea = new CustomTextArea();
        textArea.setText(text);

        VBox.setMargin(textArea, new javafx.geometry.Insets(0, 0, 15, 0));
        textArea.setEditable(true);
        return textArea;
    }

    public void addNewReplica(int index) {
        Replica replica = new Replica("", speakers.getFirst(), 0);
        StoryPoint storyPoint = new AddReplica(textAreaContainer, formReplicaView(replica), index);
        storyPoint.apply();
        editStory.addLast(storyPoint);
    }

    public int getIndex(BasePane basePane) {
        return textAreaContainer.getChildren().indexOf(basePane);
    }

    protected BasePane formReplicaView(Replica replica) {
        ComboBox<Speaker> comboBox = new SearchableComboBox(speakers, replica.getSpeaker());
        TextArea textArea = initTextArea(replica.getText());
        BasePane basepane = new TimeCodeBasePane(comboBox, textArea, TimeFormatter.format(replica.getTimecode()), this);
        VBox.setMargin(basepane, new Insets(10, 50, 0, 50));
        return basepane;
    }

    private void initButtonsOverlay() {
        buttonsOverlay = new Pane();
        buttonsOverlay.setMouseTransparent(false);
        buttonsOverlay.setPrefSize(10, 10);

        delete = new Button("Удалить выбранные");
        delete.setLayoutY(160);
        delete.setLayoutX(850);
        delete.setStyle("-fx-background-color: #2A55D5; -fx-background-radius: 16; -fx-text-fill: white;" +
                " -fx-font-size: 14px; -fx-font-family: \"Manrope Regular\";");
        delete.setFont(manropeFont1);
        delete.setVisible(true);
        delete.setOnAction(e -> {
            removeReplicas();
        });

        arrow.setFitHeight(18);
        arrow.setFitWidth(18);

        file = new Button("Файл", arrow);
        file.setLayoutY(-45);
        file.setLayoutX(230);
        file.setStyle("-fx-background-color: #0A2A85; -fx-background-radius: 8; -fx-text-fill: white;" +
                " -fx-font-size: 16px; -fx-font-family: \"Manrope Medium\";");
        file.setFont(manropeFont2);
        file.setContentDisplay(ContentDisplay.RIGHT);
        file.setOnAction(e -> {
            filePane.setVisible(!filePane.isVisible());
        });

        filePane = new Pane();
        filePane.setLayoutY(-5);
        filePane.setLayoutX(230);
        filePane.setPrefSize(200, 100);
        filePane.setStyle("-fx-background-color: #0A2A85; -fx-background-radius: 24;");
        filePane.setVisible(false);

        save = new Button("Сохранить");
        saveAs = new Button("Сохранить как");

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
        initButtonsActions(save, saveAs);
        buttonsOverlay.getChildren().addAll(delete, file, filePane);
    }

    private void removeReplicas() {
        StoryPoint storyPoint = new RemoveReplicas(textAreaContainer, getToRemove());
        storyPoint.apply();
        editStory.addLast(storyPoint);
        updateDeleteButtonVisibility();
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

    protected abstract void initTextArea();
    protected abstract void initButtonsActions(Button save, Button saveAs);
}
