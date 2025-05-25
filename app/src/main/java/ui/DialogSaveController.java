package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class DialogSaveController {
    @FXML
    private TextField fileName;
    @FXML
    private Button saveButton;

    public void initialize() {
        String file = fileName.getText();
        saveButton.setOnAction(e -> {

        });
    }

    public DialogSaveController() {

    }
}
