package ui;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import logic.general.Replica;

public class CustomTextArea extends TextArea {
    private Replica replica;

    public CustomTextArea(Replica replica) {
        this.replica = replica;

        setWrapText(true);
        setPrefRowCount(1);
        setMinHeight(Region.USE_PREF_SIZE);
        setMaxHeight(Double.MAX_VALUE);
        setPrefHeight(Region.USE_COMPUTED_SIZE);
        getStyleClass().add("custom-text-area");

        Text helper = new Text();
        helper.setWrappingWidth(getWidth() - 20);
        helper.setText(getText());

        widthProperty().addListener((obs, oldWidth, newWidth) -> {
            helper.setWrappingWidth(newWidth.doubleValue() - 20);
            helper.setText(getText() + "\n ");
            setPrefHeight(helper.getLayoutBounds().getHeight() + 20);
        });

        textProperty().addListener((obs, oldText, newText) -> {
            helper.setText(newText + "\n ");
            double height = helper.getLayoutBounds().getHeight() + 20;
            setPrefHeight(height);
        });
        setPrefRowCount(3);
    }

    public Replica getReplica() {
        return replica;
    }
}
