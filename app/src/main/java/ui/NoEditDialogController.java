package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class NoEditDialogController {
    @FXML
    private Button loadFromVideo;
    @FXML
    private Button loadFromSaves;

    private BaseController baseController;

    public NoEditDialogController(BaseController baseController) {
        this.baseController = baseController;
    }

    @FXML
    public void initialize() {
        loadFromSaves.setOnAction(e -> {
            baseController.switchToSavePane();
            baseController.closeDialog();
        });
        loadFromVideo.setOnAction(e -> {
            baseController.switchToLoadPane();
            baseController.closeDialog();
        });
    }
}
