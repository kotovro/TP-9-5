package ui.custom_elements;

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

import java.util.List;

public class SearchableDropdownComboBox extends ComboBox<Speaker> {
    private final FilteredList<Speaker> filteredSpeakers;
    private final TextField searchField = new TextField();
    private String defaultText;

    public SearchableDropdownComboBox(List<Speaker> speakers, String defaultText) {
        ObservableList<Speaker> originalSpeakers = FXCollections.observableArrayList(speakers);
        this.filteredSpeakers = new FilteredList<>(originalSpeakers, p -> true);
        this.defaultText = defaultText;

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
                    setStyle("-fx-background-color: #9DA0FA; -fx-text-fill: white; -fx-border-color: #6366B5; -fx-border-width: 0 0 1px 0;");
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
                if (empty || speaker == null) {
                    setGraphic(null);
                    setText(defaultText);
                } else {
                    imageView.setImage(speaker.getImage());
                    setGraphic(imageView);
                    setText(speaker.getName());
                }
                setStyle("-fx-text-fill: white;");
            }
        });

        setPrefWidth(160);
        setPrefHeight(32);
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
            if (event.getCode() == KeyCode.ENTER && !filteredSpeakers.isEmpty()) {
                this.setValue(filteredSpeakers.getFirst());
                this.hide();
            } else {
                //TODO: make add speaker
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