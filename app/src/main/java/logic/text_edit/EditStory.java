package logic.text_edit;

import logic.text_edit.action.Executable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//TODO add own double linked list for last action index tracking
public class EditStory {

    List<Executable> executables = new ArrayList<>();
    int currentActionIndex = -1;

    public boolean isUndoPossible()
    {
        return  (!executables.isEmpty() && currentActionIndex >= 0);
    }
    public void undoLast() {
        if (isUndoPossible()) {
            Executable lastAction = executables.get(currentActionIndex);
            lastAction.unapply();
            currentActionIndex--;
        }
    }

    public void addLast(Executable action) {
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
            Executable nextAction = executables.get(currentActionIndex);
            nextAction.apply();
        }
    }

}
