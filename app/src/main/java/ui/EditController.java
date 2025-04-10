package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import logic.text_edit.EditStory;
import ui.buttons.ButtonPressedActionProvider;

public class EditController {
    public EditStory editStory = new EditStory();
    @FXML
    private Label welcomeText;

    @FXML
    private Button findButton;

    @FXML
    private Button undoButton;

    @FXML
    private Button redoButton;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX!");
    }

    @FXML
    public void initialize() {
        ButtonPressedActionProvider actionProvider = new ButtonPressedActionProvider();
        actionProvider.attachActions(findButton, undoButton, redoButton);
    }
}