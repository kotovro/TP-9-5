package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import ui.hotkeys.HotkeysPressedActionProvider;

public class EditController {

    @FXML
    private VBox rootPane;

    @FXML
    private Label welcomeText;


    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX!");
    }

    @FXML
    public void initialize() {
        HotkeysPressedActionProvider actionProvider = new HotkeysPressedActionProvider();
        rootPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                actionProvider.attachHotkeys(newScene);
            }
        });
    }
}