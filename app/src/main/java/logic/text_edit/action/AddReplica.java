package logic.text_edit.action;

import javafx.scene.layout.VBox;
import ui.custom_elements.BasePane;

public class AddReplica implements StoryPoint {
    private final VBox textAreaContainer;
    private final BasePane basePane;
    private final int index;

    public AddReplica(VBox textAreaContainer, BasePane basePane, int index) {
        this.textAreaContainer = textAreaContainer;
        this.basePane = basePane;
        this.index = index;
    }

    @Override
    public void apply() {
        textAreaContainer.getChildren().add(index, basePane);
    }

    @Override
    public void unapply() {
        textAreaContainer.getChildren().remove(basePane);
    }
}