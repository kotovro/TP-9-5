package logic.text_edit.action;

import javafx.scene.layout.VBox;
import ui.custom_elements.BasePane;

import java.util.ArrayList;
import java.util.List;

public class RemoveReplicas implements StoryPoint {
    private final VBox textAreaContainer;
    private final List<BasePane> basePanes;
    private final List<Integer> toRemove;

    // Необходимо передавать индексы в порядке возрастания
    public RemoveReplicas(VBox textAreaContainer, List<Integer> indexes) {
        this.textAreaContainer = textAreaContainer;
        toRemove = indexes;
        this.basePanes = new ArrayList<>();
        for (Integer index : indexes) {
            basePanes.add((BasePane) textAreaContainer.getChildren().get(index));
        }
    }

    @Override
    public void apply() {
        for (BasePane basePane : basePanes) {
            textAreaContainer.getChildren().remove(basePane);
        }
    }

    @Override
    public void unapply() {
        for (int i = 0; i < basePanes.size(); i++) {
            textAreaContainer.getChildren().add(toRemove.get(i), basePanes.get(i));
        }
    }
}
