package logic.text_edit.action;

import javafx.scene.layout.VBox;
import ui.custom_elements.BasePane;

public class RemoveReplica implements StoryPoint {
    private final VBox textAreaContainer;
    private final BasePane basePane;
    private final int index;

    public RemoveReplica(VBox textAreaContainer, BasePane basePane, int index) {
        this.textAreaContainer = textAreaContainer;
        this.basePane = basePane;
        this.index = index;
    }

    @Override
    public void apply() {
        textAreaContainer.getChildren().remove(basePane);
    }

    @Override
    public void unapply() {
        textAreaContainer.getChildren().add(index, basePane);
    }
}