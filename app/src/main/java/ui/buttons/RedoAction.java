package ui.buttons;

import javafx.scene.control.Alert;

public class RedoAction implements IAction {
    public void execute() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Redo Action");
        alert.setHeaderText(null);
        alert.setContentText("Ctrl + Shift + Z pressed: Redoing...");
        alert.showAndWait();
    }
}
