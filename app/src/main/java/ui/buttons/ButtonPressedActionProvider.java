package ui.buttons;

import javafx.scene.control.Button;

public class ButtonPressedActionProvider {
    private final IAction findAction = new FindAction();
    private final IAction undoAction = new UndoAction();
    private final IAction redoAction = new RedoAction();

    public void attachActions(Button findButton, Button undoButton, Button redoButton) {
        findButton.setOnAction(event -> findAction.execute());
        undoButton.setOnAction(event -> undoAction.execute());
        redoButton.setOnAction(event -> redoAction.execute());
    }

}
