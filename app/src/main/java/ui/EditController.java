package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import logic.text_edit.EditStory;
import javafx.scene.layout.AnchorPane;
import ui.hotkeys.ButtonPressedActionProvider;

public class EditController {
    public EditStory editStory = new EditStory();

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Label welcomeText;


    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX!");
    }

    @FXML
    public void initialize() {
        ButtonPressedActionProvider actionProvider = new ButtonPressedActionProvider();
        rootPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                actionProvider.attachHotkeys(newScene);
            }
        });
    }
}