package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import logic.text_edit.EditStory;

public class EditController {
    public EditStory editStory = new EditStory();
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX!");
    }
}