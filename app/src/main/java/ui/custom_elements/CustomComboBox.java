package ui.custom_elements;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import logic.general.Speaker;

import java.util.List;

public class CustomComboBox extends ComboBox<Speaker> {
    public CustomComboBox(List<Speaker> speakers, Speaker defaultSpeaker) {
        getItems().addAll(speakers);
        getStyleClass().add("custom-combobox");

        setCellFactory(lv -> new ListCell<>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitHeight(20);
                imageView.setFitWidth(20);
                javafx.scene.shape.Circle clip = new javafx.scene.shape.Circle(10, 10, 10);
                imageView.setClip(clip);
            }

            @Override
            protected void updateItem(Speaker speaker, boolean empty) {
                super.updateItem(speaker, empty);
                if (empty || speaker == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    imageView.setImage(speaker.getImage());
                    setGraphic(imageView);
                    setText(speaker.getName());
                    setStyle("-fx-background-color: #7AA3FA; -fx-text-fill: white; -fx-border-color: #4A6DB5; -fx-border-width: 0 0 2px 0;");
                }
            }
        });

        setButtonCell(new ListCell<>() {
            private final ImageView imageView = new ImageView();

            {
                imageView.setFitHeight(20);
                imageView.setFitWidth(20);
                javafx.scene.shape.Circle clip = new javafx.scene.shape.Circle(10, 10, 10);
                imageView.setClip(clip);
            }

            @Override
            public void updateItem(Speaker speaker, boolean empty) {
                super.updateItem(speaker, empty);
                if (empty || speaker == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    imageView.setImage(speaker.getImage());
                    setGraphic(imageView);
                    setText(speaker.getName());
                    setStyle("-fx-text-fill: white;");
                }
            }
        });

        VBox.setMargin(this, new javafx.geometry.Insets(0, 0, 5, 0));
        getSelectionModel().select(speakers.indexOf(defaultSpeaker));
    }

    //это наверное вообще удалить, зачем это вообще нужно теперь
    public static class CardView extends StackPane {

        private boolean selected = false;

        public CardView(String name, String date) {
            setPadding(new Insets(15));
            setPrefSize(820, 100);
            setMaxSize(820, 100);
            setStyle(getDefaultStyle());

            setEffect(new DropShadow(10, Color.GRAY));

            VBox content = new VBox(10);
            content.setAlignment(Pos.TOP_LEFT);

            Label namel = new Label(name);
            namel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");

            Label datel = new Label(date);
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

        public String getnameText() {
            return ((Label)((VBox)getChildren().get(0)).getChildren().get(0)).getText();
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
    }
}
