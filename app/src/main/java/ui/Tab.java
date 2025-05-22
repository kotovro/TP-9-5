package ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class Tab extends HBox {
    private TranscriptDisplayer transcriptDisplayer;
    private Label name = new Label();
    private ImageView close = new ImageView();
    private EditWindowController editWindowController;

    public Tab(TranscriptDisplayer transcriptDisplayer, EditWindowController controller) {
        this.transcriptDisplayer = transcriptDisplayer;
        this.editWindowController = controller;
        init();
    }

    public void setActive() {
        setStyle("-fx-background-color: #0B0F85; -fx-background-radius: 11 11 0 0");
        name.setStyle("-fx-font-family: 'Manrope'; -fx-font-size: 16; -fx-text-fill: white;");
        setMargin(name, new Insets(16, 6, 6, 20));
        setMargin(close, new Insets(12, 6, 6, 6));
        HBox.setMargin(this, new Insets(10, 10, 0, 10));
//        transcriptDisplayer.setupHotkeys();

    }

    public void setInactive() {
        setStyle("-fx-background-color: #CDCEFF; -fx-background-radius: 11");
        name.setStyle("-fx-font-family: 'Manrope'; -fx-font-size: 14; -fx-text-fill: black;");
        setMargin(name, new Insets(14, 6, 6, 20));
        setMargin(close, new Insets(8, 6, 6, 6));
        HBox.setMargin(this, new Insets(10, 10, 10, 10));
//        transcriptDisplayer.unbindHotKeys();
    }

    public TranscriptDisplayer getTranscriptDisplayer() {
        return transcriptDisplayer;
    }

    private void init() {
        setPrefHeight(49);
        name.setText(transcriptDisplayer.getName());
        close.setImage(new Image("/images/CloseCircle1.png"));
        close.setFitHeight(32);
        close.setFitWidth(32);
        getChildren().addAll(this.name, this.close);
        setInactive();
        initListeners();
    }

    private void initListeners() {
        setOnMouseClicked(event -> {
            if (event.getTarget() == close) {
                return;
            }
            editWindowController.setActive(this);
        });

        close.setOnMouseClicked(event -> {
            editWindowController.removeTab(this);
        });
    }
}
