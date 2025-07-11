package ui.custom_elements.combo_boxes;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import logic.general.Speaker;
import logic.persistence.DBInitializer;
import ui.BaseController;

import java.util.List;

public class SearchableComboBox extends ComboBox<Speaker> {
    private static final Speaker ADD_NEW_SPEAKER = new Speaker("Добавить нового...", DBInitializer.getAddNew(), -1);

    private final List<Speaker> speakers;
    private FilteredList<Speaker> filteredSpeakers;
    private final TextField searchField = new TextField();
    private String defaultText;
    private Speaker selectedSpeaker = null;
    private final BaseController baseController;

    public SearchableComboBox(List<Speaker> speakers, Speaker defaultSpeaker, BaseController baseController) {
        this.baseController = baseController;
        this.speakers = speakers;
        selectedSpeaker = defaultSpeaker;
        ObservableList<Speaker> originalSpeakers = FXCollections.observableArrayList(speakers);
        this.filteredSpeakers = new FilteredList<>(originalSpeakers, p -> true);
        this.defaultText = "";
        init();
        this.getSelectionModel().select(defaultSpeaker);
    }

    public SearchableComboBox(List<Speaker> speakers, String defaultText, BaseController baseController) {
        this.baseController = baseController;
        this.speakers = speakers;
        ObservableList<Speaker> originalSpeakers = FXCollections.observableArrayList(speakers);
        this.filteredSpeakers = new FilteredList<>(originalSpeakers, p -> true);
        this.defaultText = defaultText;
        init();
    }

    private void init() {
        setPrefWidth(250);
        setMinWidth(250);
        configureSearchField();
        configureCellFactory();
        setupEventHandlers();
        updateComboBoxItems();
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    private void configureSearchField() {
        searchField.setPromptText("Поиск...");
        searchField.setFocusTraversable(true);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                if (newVal == null || newVal.isEmpty()) {
                    filteredSpeakers.setPredicate(s -> true);
                } else {
                    String searchText = newVal.toLowerCase();
                    filteredSpeakers.setPredicate(speaker ->
                            speaker.getName().toLowerCase().contains(searchText));
                }
                updateComboBoxItems();
            });
        });

        searchField.addEventFilter(MouseEvent.MOUSE_ENTERED, event -> {
            event.consume();
            searchField.requestFocus();
        });
    }

    private void updateComboBoxItems() {
        ObservableList<Speaker> items = FXCollections.observableArrayList();
        items.add(null);
        items.addAll(filteredSpeakers);
        items.add(ADD_NEW_SPEAKER);
        setItems(items);

        adjustDropdownHeight();
    }

    private void adjustDropdownHeight() {
        int visibleRows = 5;
        this.setVisibleRowCount(visibleRows);
    }

    private void configureCellFactory() {
        setCellFactory(lv -> new ListCell<>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitHeight(20);
                imageView.setFitWidth(20);
                javafx.scene.shape.Circle clip = new javafx.scene.shape.Circle(10, 10, 10);
                imageView.setClip(clip);
            }

            @Override
            protected void updateItem(Speaker speaker, boolean empty) {
                super.updateItem(speaker, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else if (speaker == null) {
                    setGraphic(searchField);
                    setText(null);
                } else {
                    imageView.setImage(speaker.getImage());
                    setGraphic(imageView);
                    setText(speaker.getName());
                    setStyle("-fx-background-color: #7AA3FA; -fx-text-fill: white; -fx-border-color: #4A6DB5; -fx-border-width: 0 0 2px 0;");
                }
            }
        });

        setButtonCell(new ListCell<>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitHeight(20);
                imageView.setFitWidth(20);
                javafx.scene.shape.Circle clip = new javafx.scene.shape.Circle(10, 10, 10);
                imageView.setClip(clip);
            }

            @Override
            public void updateItem(Speaker speaker, boolean empty) {
                super.updateItem(speaker, empty);
                setStyle("-fx-text-fill: white;");
                if (empty || speaker == null) {
                    if (!defaultText.isEmpty()) {
                        setText(defaultText);
                        return;
                    }
                    if (selectedSpeaker == null) {
                        setGraphic(null);
                    } else {
                        imageView.setImage(selectedSpeaker.getImage());
                        setText(selectedSpeaker.getName());
                    }
                } else {
                    if (speaker == ADD_NEW_SPEAKER) {
                        addNewSpeaker(searchField.getText());
                        getSelectionModel().clearSelection();
                        return;
                    }
                    selectedSpeaker = speaker;
                    imageView.setImage(speaker.getImage());
                    setGraphic(imageView);
                    setText(speaker.getName());
                }
            }
        });
        getStyleClass().add("custom-combobox");
        VBox.setMargin(this, new javafx.geometry.Insets(0, 0, 5, 0));
    }

    private void setupEventHandlers() {
        this.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                searchField.textProperty().setValue("");
                this.hide();
            }
        });

        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (!filteredSpeakers.isEmpty()) {
                    setValue(filteredSpeakers.getFirst());
                    hide();
                } else {
                    addNewSpeaker(searchField.getText());
                }
            }
        });

        this.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                this.hide();
            }
        });

        this.setOnShowing(e -> {
            filteredSpeakers = new FilteredList<>(FXCollections.observableArrayList(speakers), p -> true);
            updateComboBoxItems();
            Platform.runLater(() -> {
                searchField.requestFocus();
                searchField.selectAll();
            });
        });
    }

    private void addNewSpeaker(String name) {
        baseController.loadSpeakerDialog(name);
    }
}