package ui.custom_elements;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import logic.general.Speaker;

import java.util.List;

public class LinkedComboBox extends ComboBox<Speaker> {
    public LinkedComboBox(List<Speaker> speakers, Speaker defaultSpeaker) {
        getItems().addAll(speakers);
        setPrefWidth(160);
        setPrefHeight(32);
        getStyleClass().add("custom-combobox");

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
                if (empty || speaker == null) {
                    setGraphic(null);
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
                Circle clip = new Circle(10, 10, 10);
                imageView.setClip(clip);
            }

            @Override
            public void updateItem(Speaker speaker, boolean empty) {
                speaker = getValue();
                imageView.setImage(speaker.getImage());
                setGraphic(imageView);
                setText(speaker.getName());
                setStyle("-fx-text-fill: white;");
            }
        });

        VBox.setMargin(this, new Insets(0, 0, 5, 0));
        setValue(defaultSpeaker);
    }
}