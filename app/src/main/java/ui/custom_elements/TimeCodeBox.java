package ui.custom_elements;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.util.StringConverter;
import logic.utils.TimeCode;

public class TimeCodeBox extends HBox {
    Spinner<Integer> hourSpinner;
    Spinner<Integer> minuteSpinner;
    Spinner<Integer> secondSpinner;

    public TimeCodeBox(TimeCode timeCode) {
        hourSpinner = createTimeSpinner(0, 23, timeCode.getHour());
        minuteSpinner = createTimeSpinner(0, 59, timeCode.getMinute());
        secondSpinner = createTimeSpinner(0, 59, timeCode.getSecond());


        Label t1 = new Label(":");
        Label t2 = new Label(":");
        Font manropeFont2 = Font.loadFont(BasePane.class.getResourceAsStream("/fonts/Manrope-Regular.ttf"), 14);
        t1.setFont(manropeFont2);
        t2.setFont(manropeFont2);
        t1.setStyle("-fx-font-size: 16px; -fx-font-family: \"Manrope Regular\";");
        t2.setStyle("-fx-font-size: 16px; -fx-font-family: \"Manrope Regular\";");

        this.getChildren().addAll(hourSpinner, t1, minuteSpinner, t2, secondSpinner);

        setAlignment(Pos.CENTER);

        setStyle("-fx-background-color: #6F88E5; -fx-background-radius: 10;");
        //setPrefSize(100, 40);
        setLayoutX(310);
        setLayoutY(22);
        setPrefSize(190, 40);
    }

    private Spinner<Integer> createTimeSpinner(int min, int max, int initial) {
        Spinner<Integer> spinner = new Spinner<>(min, max, initial);
        spinner.setEditable(true);

        TextFormatter<Integer> formatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty() || newText.matches("\\d{1,2}")) {
                try {
                    int value = newText.isEmpty() ? min : Integer.parseInt(newText);
                    if (value >= min && value <= max) {
                        return change;
                    }
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });

        spinner.getEditor().setTextFormatter(formatter);
        spinner.getEditor().focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String text = spinner.getEditor().getText();
                if (text.isEmpty()) {
                    spinner.getValueFactory().setValue(min);
                } else {
                    try {
                        int value = Integer.parseInt(text);
                        if (value < min) spinner.getValueFactory().setValue(min);
                        else if (value > max) spinner.getValueFactory().setValue(max);
                        else spinner.getValueFactory().setValue(value);
                    } catch (NumberFormatException e) {
                        spinner.getValueFactory().setValue(min);
                    }
                }
            }
        });

        spinner.getValueFactory().setConverter(new StringConverter<Integer>() {
            @Override
            public String toString(Integer value) {
                return String.format("%02d", value);
            }

            @Override
            public Integer fromString(String string) {
                try {
                    return Integer.parseInt(string);
                } catch (NumberFormatException e) {
                    return min;
                }
            }
        });

        return spinner;
    }

    public TimeCode getTimeCode() {
        return new TimeCode(hourSpinner.getValue(), minuteSpinner.getValue(), secondSpinner.getValue());
    }
}
