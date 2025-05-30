package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ExitDialogController {
    @FXML
    private Button yesButton;
    @FXML
    private Button noButton;

    private BaseController baseController;

    public ExitDialogController(BaseController baseController) {
        this.baseController = baseController;
    }

    @FXML
    public void initialize() {
        yesButton.setOnAction(e -> {
            System.exit(0);
        });
        noButton.setOnAction(e -> {
            baseController.closeDialog();
        });
    }
}
