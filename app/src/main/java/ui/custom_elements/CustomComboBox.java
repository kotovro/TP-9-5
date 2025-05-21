package ui.custom_elements;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import logic.general.Speaker;

import java.util.List;

public class CustomComboBox extends ComboBox<Speaker> {
    public CustomComboBox(List<Speaker> speakers, Speaker defaultSpeaker) {
        getItems().addAll(speakers);
        setPrefWidth(160);
        setPrefHeight(32);
        getStyleClass().add("custom-combobox");



        VBox.setMargin(this, new javafx.geometry.Insets(0, 0, 5, 0));
        getSelectionModel().select(speakers.indexOf(defaultSpeaker));
    }
}
