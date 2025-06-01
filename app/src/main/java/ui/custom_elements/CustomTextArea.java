package ui.custom_elements;

import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import logic.general.Replica;

public class CustomTextArea extends TextArea {

    public CustomTextArea() {
        setWrapText(true);
        setMinHeight(Region.USE_PREF_SIZE);
        setMaxHeight(Double.MAX_VALUE);
        setPrefHeight(Region.USE_COMPUTED_SIZE);
        getStyleClass().add("custom-text-area");

//        Text helper = new Text();
//        helper.setWrappingWidth(getWidth() - 10);
//        helper.setFont(getFont());
//        helper.setText(getText());
//
//        widthProperty().addListener((obs, oldWidth, newWidth) -> {
//            helper.setWrappingWidth(newWidth.doubleValue() - 10);
//            helper.setText(getText() + "\n ");
//            setPrefHeight(helper.getLayoutBounds().getHeight() + 40);
//        });
//
//        textProperty().addListener((obs, oldText, newText) -> {
//            helper.setText(newText + "\n ");
//            double height = helper.getLayoutBounds().getHeight() + 40;
//            setPrefHeight(height);
//        });
        widthProperty().addListener((obs, oldVal, newVal) -> {
            setPrefHeight(computeTextHeight(this));
        });
        textProperty().addListener((observable, oldValue, newValue) -> {
            setPrefHeight(computeTextHeight(this));
        });
    }

    private double computeTextHeight(TextArea textArea) {
        Text text = new Text(textArea.getText());
        text.setFont(textArea.getFont());
        text.setWrappingWidth(textArea.getWidth() - 10);
        return text.getLayoutBounds().getHeight() * 1.075 + 25;
    }
}
