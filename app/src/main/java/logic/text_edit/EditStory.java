package logic.text_edit;

import logic.text_edit.action.StoryPoint;

import java.util.ArrayList;
import java.util.List;

//TODO add own double linked list for last action index tracking
public class EditStory {

    List<StoryPoint> executables = new ArrayList<>();
    int currentActionIndex = -1;

    public boolean canUndo() {
        return  (!executables.isEmpty() && currentActionIndex >= 0);
    }

    public void undoLast() {
        if (canUndo()) {
            StoryPoint lastAction = executables.get(currentActionIndex);
            lastAction.unapply();
            currentActionIndex--;
        }
    }

    public void addLast(StoryPoint action) {
        if (currentActionIndex < executables.size() - 1) {
            executables.subList(currentActionIndex + 1, executables.size()).clear();
        }
        executables.addLast(action);
    }

    public boolean isRedoPossible() {
        return currentActionIndex + 1 < executables.size();
    }

    public void redoLast() {
        if(isRedoPossible()) {
            currentActionIndex++;
            StoryPoint nextAction = executables.get(currentActionIndex);
            nextAction.apply();
        }
    }

}
