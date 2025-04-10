package ui.buttons;

import javafx.scene.control.Alert;



public class UndoAction implements IAction {
    public void execute() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Undo Action");
        alert.setHeaderText(null);
        alert.setContentText("Ctrl + Z pressed: Undoing...");
        alert.showAndWait();
    }
}
