package ui.custom_elements;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Objects;

public class BasePane extends Pane {
    public ComboBox combobox;
    public TextArea textarea;
    public ImageView deleteButton;
    public CheckBox cb;

    public BasePane(ComboBox combobox, TextArea textarea, ImageView deleteButton, CheckBox cb) {
        this.combobox = combobox;
        this.textarea = textarea;
        this.deleteButton = deleteButton;
        this.cb = cb;
        this.setWidth(750);
        this.setHeight(154);
        Pane p = new Pane();
        p.setPrefWidth(860);
        p.setPrefHeight(83);

        this.textarea.prefHeightProperty().addListener((obs, oldHeight, newHeight) -> {
            p.setPrefHeight(newHeight.doubleValue() + 40);
        });
        this.textarea.setPrefWidth(820);
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


        Image image = new Image(Objects.requireNonNull(getClass().getResource("/images/CloseCircle2.png")).toExternalForm());
        deleteButton.setImage(image);

        this.cb.setLayoutX(0);
        this.cb.setLayoutY(110);
        this.cb.setPrefSize(18, 18);
        this.getChildren().addAll(combobox, p, cb);

    }

    public boolean isSelected() {
        return true;
    }
}
