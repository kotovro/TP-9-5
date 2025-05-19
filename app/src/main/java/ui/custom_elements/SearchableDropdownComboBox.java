package ui.custom_elements;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import logic.general.Speaker;

public class SearchableDropdownComboBox extends ComboBox<Speaker> {
    private final FilteredList<Speaker> filteredSpeakers;
    private final TextField searchField = new TextField();

    public SearchableDropdownComboBox(ObservableList<Speaker> speakers, Speaker defaultSpeaker) {
        ObservableList<Speaker> originalSpeakers = FXCollections.observableArrayList(speakers);
        this.filteredSpeakers = new FilteredList<>(originalSpeakers, p -> true);

        configureSearchField();
        configureCellFactory();
        setupEventHandlers();
        updateComboBoxItems();
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
    }

    private void updateComboBoxItems() {
        ObservableList<Speaker> items = FXCollections.observableArrayList();
        items.add(null);
        items.addAll(filteredSpeakers);
        setItems(items);
        setVisibleRowCount(Math.min(5, Math.max(1, filteredSpeakers.size())));
    }

    private void configureCellFactory() {
        this.setCellFactory(param -> new ListCell<Speaker>() {
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
                    setGraphic(null);
                    setText(speaker.getName());
                }
            }
        });

        this.setButtonCell(new ListCell<Speaker>() {
            @Override
            protected void updateItem(Speaker speaker, boolean empty) {
                super.updateItem(speaker, empty);
                setText(empty || speaker == null ? "" : speaker.getName());
            }
        });
    }

    private void setupEventHandlers() {
        this.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                searchField.textProperty().setValue("");
                this.hide();
            }
        });

        this.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                this.hide();
            }
        });

        this.setOnShowing(e -> Platform.runLater(() -> {
            searchField.requestFocus();
            searchField.selectAll();
        }));
    }
}