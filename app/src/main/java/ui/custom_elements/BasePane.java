package ui.custom_elements;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import logic.general.Replica;
import logic.general.Speaker;

import java.util.Objects;

public class BasePane extends Pane {
    private static final Font manropeFont2 = Font.loadFont(BasePane.class.getResourceAsStream("/fonts/Manrope-Regular.ttf"), 16);
    private ComboBox<Speaker> combobox;
    private TextArea textarea;
    private CheckBox checkBox;
    private ImageView addButton;
    private Pane timeCode;

    public BasePane(ComboBox<Speaker> combobox, TextArea textarea, BaseDisplayer baseDisplayer) {
        this.combobox = combobox;
        this.textarea = textarea;
        this.checkBox = new CheckBox();
        this.addButton = new ImageView();
        this.timeCode = new Pane();
        this.setWidth(750);
        this.setHeight(154);
        Pane p = new Pane();
        p.setPrefWidth(860);
        p.setPrefHeight(83);

        this.timeCode.setStyle("-fx-background-color: #6F88E5; -fx-background-radius: 10;");
        this.timeCode.setPrefSize(80, 34);
        this.timeCode.setLayoutX(310);
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


        Image image = new Image(Objects.requireNonNull(getClass().getResource("/images/DocumentMedicine.png")).toExternalForm());
        this.addButton.setImage(image);
        this.addButton.setFitHeight(38);
        this.addButton.setFitWidth(38);
        this.addButton.setLayoutX(400);
        this.addButton.setLayoutY(20);

        Pane add = getAddPane(baseDisplayer);
        addButton.setOnMouseClicked(e -> {
            add.setVisible(!add.isVisible());
        });

        checkBox.setLayoutX(0);
        checkBox.setLayoutY(110);
        checkBox.setPrefSize(18, 18);
        checkBox.setOnAction(e -> baseDisplayer.updateDeleteButtonVisibility());
        getChildren().addAll(combobox, p, checkBox, timeCode, addButton, add);

    }

    public boolean isSelected() {
        return checkBox.isSelected();
    }

    public Replica getReplica() {
        Speaker selected = combobox.getSelectionModel().getSelectedItem();
        System.out.println(combobox.getItems().getFirst().getName());
        return new Replica(textarea.getText(), selected == null ? combobox.getItems().getFirst() : selected);
    }

    private Pane getAddPane(BaseDisplayer baseDisplayer) {
        Pane addPane = new Pane();
        addPane.setLayoutX(450);
        addPane.setLayoutY(0);
        addPane.setPrefSize(150, 80);
        addPane.setStyle("-fx-background-color: #0A2A85; -fx-background-radius: 16;");
        addPane.setVisible(false);

        Button up = new Button("Добавить сверху");
        up.setLayoutX(5);
        up.setLayoutY(5);
        up.setStyle("-fx-background-color: #2A55D5; -fx-background-radius: 16; -fx-text-fill: white;" +
                " -fx-font-size: 14px; -fx-font-family: \"Manrope Regular\";");
        up.setFont(manropeFont2);
        up.setOnAction(event -> {
            baseDisplayer.addNewReplica(baseDisplayer.getIndex(this));
        });

        Button down = new Button("Добавить снизу");
        down.setLayoutX(5);
        down.setLayoutY(45);
        down.setStyle("-fx-background-color: #2A55D5; -fx-background-radius: 16; -fx-text-fill: white;" +
                " -fx-font-size: 14px; -fx-font-family: \"Manrope Regular\";");
        down.setFont(manropeFont2);
        down.setOnAction(event -> {
            baseDisplayer.addNewReplica(baseDisplayer.getIndex(this) + 1);
        });

        addPane.getChildren().addAll(up, down);
        return addPane;
    }
}
