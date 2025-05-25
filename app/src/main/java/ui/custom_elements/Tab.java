package ui.custom_elements;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import ui.BaseController;
import ui.EditWindowController;

public class Tab extends HBox {
    private static final Image closeCircle1 = new Image(BaseController.class.getResource("/images/CloseCircle1.png").toExternalForm());
    private static final Font manropeFont = Font.loadFont(Tab.class.getResourceAsStream("/fonts/Manrope-Bold.ttf"), 16);
    private EditableDisplayer transcriptDisplayer;
    private Label name = new Label();
    private ImageView close = new ImageView();
    private EditWindowController editWindowController;

    public Tab(EditableDisplayer transcriptDisplayer, EditWindowController controller) {
        this.transcriptDisplayer = transcriptDisplayer;
        this.editWindowController = controller;
        init();
    }

    public void setActive() {
        this.setStyle("-fx-background-color: #0A2A85; -fx-background-radius: 11 11 0 0");
        Font manropeFont = Font.loadFont(getClass().getResourceAsStream("/fonts/Manrope-Bold.ttf"), 16);
        name.setStyle("-fx-font-family: \"Manrope Bold\";-fx-font-size: 16; -fx-text-fill: white;");
        name.setFont(manropeFont);
        setMargin(name, new Insets(16, 6, 6, 20));
        setMargin(close, new Insets(12, 6, 6, 6));
        HBox.setMargin(this, new Insets(10, 10, 0, 10));
//        transcriptDisplayer.setupHotkeys();

    }

    public void setInactive() {
        this.setStyle("-fx-background-color: #B8D0FF; -fx-background-radius: 11");
        name.setFont(manropeFont);
        name.setStyle("-fx-font-family: \"Manrope Bold\"; -fx-font-size: 14; -fx-text-fill: black;");
        setMargin(name, new Insets(14, 6, 6, 20));
        setMargin(close, new Insets(8, 6, 6, 6));
        HBox.setMargin(this, new Insets(10, 10, 10, 10));
//        transcriptDisplayer.unbindHotKeys();
    }

    public EditableDisplayer getTranscriptDisplayer() {
        return transcriptDisplayer;
    }

    private void init() {
        setPrefHeight(49);
        name.setText(transcriptDisplayer.getName());
        close.setImage(closeCircle1);
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
