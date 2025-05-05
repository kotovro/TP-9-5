package logic.text_edit.action;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import logic.general.Speaker;

public class RemoveReplica implements StoryPoint {
    private final VBox textAreaContainer;
    private final ComboBox<Speaker> comboBox;
    private final TextArea textArea;
    private final int index;

    public RemoveReplica(VBox textAreaContainer, ComboBox<Speaker> comboBox, TextArea textArea, int index) {
        this.textAreaContainer = textAreaContainer;
        this.comboBox = comboBox;
        this.textArea = textArea;
        this.index = index;
    }

    @Override
    public void apply() {
        textAreaContainer.getChildren().remove(comboBox);
        textAreaContainer.getChildren().remove(textArea);
    }

    @Override
    public void unapply() {
        textAreaContainer.getChildren().add(index, textArea);
        textAreaContainer.getChildren().add(index, comboBox);
    }
}