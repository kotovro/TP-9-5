package ui.custom_elements;

import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;

public interface EditableDisplayer {
    void setupPane(ScrollPane replicas);
    void setupHotkeys();
    void unbindHotKeys();
    String getName();
}
