package ui.custom_elements;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import logic.general.Transcript;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CardView extends StackPane {
    private boolean selected = false;
    private final Transcript transcript;

    public CardView(Transcript transcript) {
        this.transcript = transcript;
        setPadding(new Insets(15));
        setPrefSize(520, 100);
        setMaxSize(520, 100);
        setStyle(getDefaultStyle());

        setEffect(new DropShadow(10, Color.GRAY));

        VBox content = new VBox(10);
        content.setAlignment(Pos.TOP_LEFT);

        Label namel = new Label(transcript.getName());
        namel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");

        Label datel = new Label(toString(transcript.getDate()));
        datel.setStyle("-fx-font-size: 16px; -fx-text-fill: #555; -fx-text-fill: black;");

        content.getChildren().addAll(namel, datel);
        getChildren().add(content);

        //setOnMouseEntered(e -> setStyle(getHoverStyle()));
        setOnMouseExited(e -> setStyle(selected ? getSelectedStyle() : getDefaultStyle()));
        setOnMouseClicked(e -> {
            if (onSelected != null) onSelected.run();
        });
    }

    private Runnable onSelected;

    public void setOnSelected(Runnable r) {
        this.onSelected = r;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        setStyle(selected ? getSelectedStyle() : getDefaultStyle());
    }

    public boolean isSelected() {
        return selected;
    }

    public Transcript getTranscript() {
        return transcript;
    }

    private String getDefaultStyle() {
        return "-fx-background-color: white; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #ddd;";
    }

    private String getHoverStyle() {
        return "-fx-background-color: #f0f0f0; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #ccc;";
    }

    private String getSelectedStyle() {
        return "-fx-background-color: #E6E6FF; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #3338D5;";
    }

    private static String toString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }
}

