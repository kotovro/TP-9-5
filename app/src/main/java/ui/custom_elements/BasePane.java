package ui.custom_elements;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.util.Objects;

public class BasePane extends Pane {
    public ComboBox combobox;
    public TextArea textarea;
    public ImageView addButton;
    public CheckBox cb;
    public Pane timeCode;

    public BasePane(ComboBox combobox, TextArea textarea, ImageView addButton, CheckBox cb, Pane timeCode) {
        this.combobox = combobox;
        this.textarea = textarea;
        this.addButton = addButton;
        this.cb = cb;
        this.timeCode = timeCode;
        this.setWidth(750);
        this.setHeight(154);
        Pane p = new Pane();
        p.setPrefWidth(860);
        p.setPrefHeight(83);

        this.timeCode.setStyle("-fx-background-color: #6F88E5; -fx-background-radius: 10;");
        this.timeCode.setPrefSize(80, 34);
        this.timeCode.setLayoutX(210);
        this.timeCode.setLayoutY(22);
        Label time = new Label("timecode");
        time.setLayoutX(10);
        time.setLayoutY(8);
        timeCode.getChildren().add(time);

        this.textarea.prefHeightProperty().addListener((obs, oldHeight, newHeight) -> {
            p.setPrefHeight(newHeight.doubleValue() + 40);
        });
        this.textarea.setPrefWidth(800);
        this.textarea.setPrefHeight(59);
        this.textarea.setLayoutX(18);
        this.textarea.setLayoutY(18);
        //textarea.setStyle("-fx-background-radius: 10; -fx-border-radius: 10;");
        p.setStyle("-fx-background-color: #6F9AE5; -fx-background-radius: 10;");
        p.getChildren().add(textarea);
        p.setLayoutX(25);
        p.setLayoutY(71);

        this.combobox.setPrefWidth(156);
        this.combobox.setPrefHeight(34);
        this.combobox.setLayoutX(14 + 25);
        this.combobox.setLayoutY(22);
        // динамически изменять размер combobox в зависимости от содержимого


        Image image = new Image(Objects.requireNonNull(getClass().getResource("/images/DocumentMedicine.png")).toExternalForm());
        this.addButton.setImage(image);
        this.addButton.setFitHeight(38);
        this.addButton.setFitWidth(38);
        this.addButton.setLayoutX(300);
        this.addButton.setLayoutY(20);

        Pane add = new Pane();
        add.setLayoutX(350);
        add.setLayoutY(20);
        add.setPrefSize(250, 80);
        add.setStyle("-fx-background-color: #0A2A85; -fx-background-radius: 16;");
        add.setVisible(false);

        Button up = new Button("Добавить сверху");
        Button down = new Button("Добавить снизу");
        up.setLayoutX(5);
        up.setLayoutY(5);
        down.setLayoutX(5);
        down.setLayoutY(45);

        Font manropeFont2 = Font.loadFont(getClass().getResourceAsStream("/fonts/Manrope-Regular.ttf"), 16);
        down.setStyle("-fx-background-color: #2A55D5; -fx-background-radius: 16; -fx-text-fill: white;" +
                " -fx-font-size: 14px; -fx-font-family: \"Manrope Regular\";");
        down.setFont(manropeFont2);

        up.setStyle("-fx-background-color: #2A55D5; -fx-background-radius: 16; -fx-text-fill: white;" +
                " -fx-font-size: 14px; -fx-font-family: \"Manrope Regular\";");
        up.setFont(manropeFont2);

        add.getChildren().addAll(up, down);

        this.addButton.setOnMouseClicked(e -> {
            if (add.isVisible()) {
                add.setVisible(false);
            }
            else {
                add.setVisible(true);
            }
        });

        this.cb.setLayoutX(0);
        this.cb.setLayoutY(110);
        this.cb.setPrefSize(18, 18);
        this.getChildren().addAll(combobox, p, cb, timeCode, addButton, add);
    }

    public boolean isSelected() {
        return true;
    }
}
