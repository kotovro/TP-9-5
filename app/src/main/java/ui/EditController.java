package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.List;
import javafx.scene.control.Label;
import logic.text_edit.EditStory;
import javafx.scene.layout.AnchorPane;
import ui.hotkeys.HotkeysPressedActionProvider;

public class EditController {
    public EditStory editStory = new EditStory();

    @FXML
    private AnchorPane rootPane;

    @FXML
    private VBox textAreaContainer;


    @FXML
    public void initialize() {
        List<Speaker> speakers = List.of(
                new Speaker("Анна", "/images/logo.png"),
                new Speaker("Борис", "/images/UserSpeak.png"),
                new Speaker("Виктор", "/images/UserSpeak2.png"),
                new Speaker("Галина", "/images/DangerCircle.png")
        );
        int replicas = 5;
        for (int i = 0; i < replicas; i++) {
            ComboBox<Speaker> comboBox = new ComboBox<>();
            comboBox.getItems().addAll(speakers);

            comboBox.setCellFactory(lv -> new ListCell<>() {
                private final ImageView imageView = new ImageView();
                {
                    imageView.setFitHeight(24);
                    imageView.setFitWidth(24);
                }

                @Override
                protected void updateItem(Speaker item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        imageView.setImage(new Image(getClass().getResourceAsStream(item.getImagePath())));
                        setGraphic(imageView);
                        setText(item.getName());
                    }
                }
            });

            comboBox.setButtonCell(new ListCell<>() {
                private final ImageView imageView = new ImageView();
                {
                    imageView.setFitHeight(24);
                    imageView.setFitWidth(24);
                }

                @Override
                protected void updateItem(Speaker item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        imageView.setImage(new Image(getClass().getResourceAsStream(item.getImagePath())));
                        setGraphic(imageView);
                        setText(item.getName());
                    }
                }
            });


            VBox.setMargin(comboBox, new javafx.geometry.Insets(0, 0, 5, 0));

            textAreaContainer.getChildren().add(comboBox);
            TextArea textArea = new TextArea();
            textArea.setText("meimviece");
            textArea.setPrefRowCount(3);
            VBox.setMargin(textArea, new javafx.geometry.Insets(0, 0, 50, 0));
            textAreaContainer.getChildren().add(textArea);

            HotkeysPressedActionProvider actionProvider = new HotkeysPressedActionProvider();
            rootPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    actionProvider.attachHotkeys(newScene);
                }
            });
        }
    }
}