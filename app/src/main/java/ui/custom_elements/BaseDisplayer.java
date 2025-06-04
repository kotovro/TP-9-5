package ui.custom_elements;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import logic.general.Replica;
import logic.general.Speaker;
import logic.text_edit.EditStory;
import logic.text_edit.action.AddReplica;
import logic.text_edit.action.RemoveReplicas;
import logic.text_edit.action.StoryPoint;
import logic.utils.EntitiesExporter;
import logic.utils.TimeFormatter;
import ui.BaseController;
import ui.custom_elements.combo_boxes.SearchableComboBox;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseDisplayer implements EditableDisplayer {
    private static final Font manropeFont1 = Font.loadFont(BaseDisplayer.class.getResourceAsStream("/fonts/Manrope-Regular.ttf"), 16);
    private static final Font manropeFont2 = Font.loadFont(BaseDisplayer.class.getResourceAsStream("/fonts/Manrope-Medium.ttf"), 16);
    private final Image arrow = new Image(BaseDisplayer.class.getResource("/images/SquareAltArrowDown2.png").toExternalForm());
    protected final List<Speaker> speakers;
    protected VBox textAreaContainer = new VBox();
    protected final Insets basePaneInsets = new Insets(10, 50, 0, 50);
    private final EditStory editStory = new EditStory();
    private final String name;
    private EventHandler<KeyEvent> keyEventHandler;

    protected Button delete;

    protected Button file;
    protected Pane filePane;
    protected Button save;
    protected Button saveAs;
    protected Button export;

    protected Button edit;
    protected Pane editPane;
    protected Button undo;
    protected Button redo;
    protected Button addReplica;

    protected final BaseController baseController;

    public BaseDisplayer(String name, List<Speaker> speakers, BaseController baseController) {
        this.name = name;
        this.speakers = speakers;
        this.baseController = baseController;
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
        this.keyEventHandler = event -> {
            if (event.getCode() == KeyCode.DELETE) {
                removeReplicas();
            } else if (event.getCode() == KeyCode.N && event.isControlDown()) {
                addNewReplica(0);
            } else if (event.getCode() == KeyCode.Z && event.isControlDown() && !(event.getTarget() instanceof TextArea)) {
                if (editStory.canUndo()) editStory.undoLast();
                updateStoryButtonsState();
            } else if (event.getCode() == KeyCode.Y && event.isControlDown() && !(event.getTarget() instanceof TextArea)) {
                if (editStory.canRedo()) editStory.redoLast();
                updateStoryButtonsState();
            }
        };

        initButtonsOverlay();
    }

    private void updateStoryButtonsState() {
        redo.setDisable(!editStory.canRedo());
        undo.setDisable(!editStory.canUndo());
    }

    public void setupOverlay(Pane fileOverlayPane, Pane deleteOverlayPane) {
        fileOverlayPane.getChildren().setAll(file, filePane, edit, editPane);
        deleteOverlayPane.getChildren().setAll(delete);
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
        updateStoryButtonsState();
        unlockSave();
    }

    public int getIndex(BasePane basePane) {
        return textAreaContainer.getChildren().indexOf(basePane);
    }

    protected BasePane formReplicaView(Replica replica) {
        ComboBox<Speaker> comboBox = new SearchableComboBox(speakers, replica.getSpeaker(), baseController);
        TextArea textArea = initTextArea(replica.getText());
        BasePane basepane = new TimeCodeBasePane(comboBox, textArea, TimeFormatter.format(replica.getTimecode()), this);
        VBox.setMargin(basepane, new Insets(10, 50, 0, 50));
        return basepane;
    }

    private void initButtonsOverlay() {
        delete = new Button("Удалить выбранные");
        delete.setStyle("-fx-background-color: #2A55D5; -fx-background-radius: 16; -fx-text-fill: white;" +
                " -fx-font-size: 14px; -fx-font-family: \"Manrope Regular\";");
        delete.setFont(manropeFont1);
        delete.setVisible(false);
        delete.setOnAction(e -> {
            removeReplicas();
        });

        ImageView arrowView = new ImageView(arrow);
        arrowView.setFitHeight(18);
        arrowView.setFitWidth(18);

        file = new Button("Файл", arrowView);
        file.setStyle("-fx-background-color: #0A2A85; -fx-background-radius: 8; -fx-text-fill: white;" +
                " -fx-font-size: 16px; -fx-font-family: \"Manrope Medium\";");
        file.setFont(manropeFont2);
        file.setContentDisplay(ContentDisplay.RIGHT);
        file.setOnAction(e -> {
            boolean isFilePaneVisible = filePane.isVisible();
            Platform.runLater(() -> {
                filePane.setVisible(!isFilePaneVisible);
            });
        });

        filePane = new Pane();
        filePane.setLayoutY(40);
        filePane.setPrefSize(150, 130);
        filePane.setStyle("-fx-background-color: #0A2A85; -fx-background-radius: 24;");
        filePane.setVisible(false);

        save = makeButton("Сохранить", 15, 10);
        saveAs = makeButton("Сохранить как", 15, 50);
        export = makeButton("Экспорт", 15, 90);

        filePane.getChildren().addAll(save, saveAs, export);
        initButtonsActions(save, saveAs, export);

        if (filePane.getScene() != null) {
            addPanesAutoClose();
        } else {
            filePane.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    addPanesAutoClose();
                }
            });
        }

        editPane = new Pane();
        editPane.setLayoutX(100);
        editPane.setLayoutY(40);
        editPane.setPrefSize(180, 130);
        editPane.setStyle("-fx-background-color: #0A2A85; -fx-background-radius: 24;");
        editPane.setVisible(false);

        arrowView = new ImageView(arrow);
        arrowView.setFitHeight(18);
        arrowView.setFitWidth(18);

        edit = new Button("Редактирование", arrowView);
        edit.setStyle("-fx-background-color: #2a55d5; -fx-background-radius: 8; -fx-text-fill: white;" +
                " -fx-font-size: 16px; -fx-font-family: \"Manrope Medium\";");
        edit.setFont(manropeFont2);
        edit.setLayoutX(100);
        edit.setContentDisplay(ContentDisplay.RIGHT);
        edit.setOnAction(e -> {
            boolean isEditPaneVisible = editPane.isVisible();
            Platform.runLater(() -> {
                editPane.setVisible(!isEditPaneVisible);
            });
        });

        addReplica = makeButton("Добавить реплику", 15, 90);
        addReplica.setOnAction(e -> {
            addNewReplica(0);
            Platform.runLater(() -> {
                editPane.setVisible(true);
            });
        });
        undo = makeButton("Отменить", 15, 10);
        undo.setOnAction(e -> {
            if (editStory.canUndo()) editStory.undoLast();
            updateStoryButtonsState();
            if (textAreaContainer.getChildren().isEmpty()) lockSave();
            if (!textAreaContainer.getChildren().isEmpty()) unlockSave();
            Platform.runLater(() -> {
                editPane.setVisible(true);
            });
        });
        redo = makeButton("Повторить", 15, 50);
        redo.setOnAction(e -> {
            if (editStory.canRedo()) editStory.redoLast();
            updateStoryButtonsState();
            if (textAreaContainer.getChildren().isEmpty()) lockSave();
            if (!textAreaContainer.getChildren().isEmpty()) unlockSave();
            Platform.runLater(() -> {
                editPane.setVisible(true);
            });
        });
        undo.setDisable(true);
        redo.setDisable(true);
        editPane.getChildren().addAll(addReplica, undo, redo);
    }

    private Button makeButton(String text, int x, int y) {
        Button button = new Button(text);
        button.setLayoutX(x);
        button.setLayoutY(y);

        button.setStyle("-fx-background-color: #2A55D5; -fx-background-radius: 16; -fx-text-fill: white;" +
                " -fx-font-size: 14px; -fx-font-family: \"Manrope Regular\";");
        button.setFont(manropeFont2);
        return button;
    }

    private void addPanesAutoClose() {
        filePane.getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            filePane.setVisible(false);
            editPane.setVisible(false);
        });
    }

    private void removeReplicas() {
        StoryPoint storyPoint = new RemoveReplicas(textAreaContainer, getToRemove());
        storyPoint.apply();
        editStory.addLast(storyPoint);
        updateStoryButtonsState();
        updateDeleteButtonVisibility();
        if (textAreaContainer.getChildren().isEmpty()) lockSave();
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
    protected abstract void unlockSave();
    protected abstract void lockSave();
    protected abstract void initButtonsActions(Button save, Button saveAs, Button export);
}
