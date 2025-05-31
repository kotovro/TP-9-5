package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import ui.custom_elements.SaveAsProvider;

public class DialogSaveController {
    @FXML
    private TextField fileName;

    @FXML
    private Pane errorPane;
    @FXML
    private Button saveButton;

    private final BaseController baseController;
    private SaveAsProvider saveAsProvider;

    public DialogSaveController(BaseController baseController) {
        this.baseController = baseController;
    }

    public void initialize() {
        saveButton.setOnAction(e -> {
            String name = fileName.getText();
            saveAsProvider.saveAs(name);
            baseController.closeDialog();
            baseController.switchToSavePane();
        });
    }

    public void setSaveAsProvider(SaveAsProvider saveAsProvider) {
        this.saveAsProvider = saveAsProvider;
    }
}
