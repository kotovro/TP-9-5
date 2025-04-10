package ui.buttons;

import javafx.scene.control.Alert;

public class FindAction implements IAction {
    public void execute() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Find Action");
        alert.setHeaderText(null);
        alert.setContentText("Ctrl + F pressed: Searching...");
        alert.showAndWait();
    }
}
