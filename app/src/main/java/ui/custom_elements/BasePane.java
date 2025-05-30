package ui.custom_elements;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import logic.general.Replica;
import logic.general.Speaker;

import java.util.Objects;

public class BasePane extends Pane {
    private static final Font manropeFont2 = Font.loadFont(BasePane.class.getResourceAsStream("/fonts/Manrope-Regular.ttf"), 16);
    protected ComboBox<Speaker> combobox;
    protected TextArea textarea;
    protected CheckBox checkBox;
    protected ImageView addButton;
    protected Pane addPane;

    public BasePane(ComboBox<Speaker> combobox, TextArea textarea, BaseDisplayer baseDisplayer) {
        this.combobox = combobox;
        this.textarea = textarea;
        this.checkBox = new CheckBox();
        this.addButton = new ImageView();
        this.setWidth(750);
        this.setHeight(154);
        Pane p = new Pane();
        p.setPrefWidth(860);
        p.setPrefHeight(83);

        this.textarea.prefHeightProperty().addListener((obs, oldHeight, newHeight) -> {
            p.setPrefHeight(newHeight.doubleValue() + 40);
        });

        this.textarea.setPrefWidth(800);
        this.textarea.setPrefHeight(59);
        this.textarea.setLayoutX(18);
        this.textarea.setLayoutY(18);

        this.textarea.textProperty().addListener((observable, oldValue, newValue) -> {
            this.textarea.setPrefHeight(computeTextHeight(this.textarea));
        });

        Platform.runLater(() -> {
            resizeTextAreaToFitContent(this.textarea);
        });

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
        this.addButton.setLayoutX(320);
        this.addButton.setLayoutY(20);

        addPane = getAddPane(baseDisplayer);
        addButton.setOnMouseClicked(e -> {
            addPane.setVisible(!addPane.isVisible());
        });

        checkBox.setLayoutX(0);
        checkBox.setLayoutY(110);
        checkBox.setPrefSize(18, 18);
        checkBox.setOnAction(e -> baseDisplayer.updateDeleteButtonVisibility());
        getChildren().addAll(combobox, p, checkBox, addButton, addPane);
    }

    public boolean isSelected() {
        return checkBox.isSelected();
    }

    public Replica getReplica() {
        Speaker selected = combobox.getSelectionModel().getSelectedItem();
        return new Replica(textarea.getText(), selected == null ? combobox.getItems().get(1) : selected, 0);
    }

    private Pane getAddPane(BaseDisplayer baseDisplayer) {
        Pane addPane = new Pane();
        addPane.setLayoutX(370);
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

    private double computeTextHeight(TextArea textArea) {
        Text text = new Text(textArea.getText());
        text.setFont(textArea.getFont());
        text.setWrappingWidth(textArea.getWidth() - 10);
        return text.getLayoutBounds().getHeight() + 40;
    }

    private void resizeTextAreaToFitContent(TextArea textArea) {
        Text text = new Text(textArea.getText());
        text.setFont(textArea.getFont());
        text.setWrappingWidth(textArea.getWidth() - 10);
        double height = text.getLayoutBounds().getHeight() + 20;

        textArea.setPrefHeight(height);
    }
}
