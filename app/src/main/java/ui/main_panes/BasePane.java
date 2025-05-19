package ui.main_panes;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

public class BasePane extends Pane {
    public ComboBox combobox;
    public TextArea textarea;
    public BasePane(ComboBox combobox, TextArea textarea) {
        this.combobox = combobox;
        this.textarea = textarea;
    }
}
