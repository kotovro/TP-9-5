package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class DialogSpeakerController {
    @FXML
    private TextField textSpeaker;
    @FXML
    private Button addButton;

    public void initialize() {
        String speaker = textSpeaker.getText();
        addButton.setOnAction(e -> {

        });
    }

    public DialogSpeakerController() {

    }
}
