package logic.text_edit;

import logic.text_edit.action.StoryPoint;

public class EditStory {
    private static final int BUFFER_MAX_SIZE = 100;

    private final StoryPoint[] executables = new StoryPoint[BUFFER_MAX_SIZE * 2];
    //Указывает на позицию, следующую за текущим story point
    private int currentActionIndex = 0;
    //Указывает на позицию, следующую за последним story point
    private int maxIndex = 0;

    public boolean canUndo() {
        return currentActionIndex > 0;
    }

    public void undoLast() {
        if (canUndo()) {
            currentActionIndex--;
            executables[currentActionIndex].unapply();
        }
    }

    public void addLast(StoryPoint action) {
        executables[currentActionIndex] = action;
        currentActionIndex++;
        maxIndex = currentActionIndex;
        if (maxIndex >= BUFFER_MAX_SIZE * 2) {
            freeBufferSpace();
        }
    }

    public boolean canRedo() {
        return currentActionIndex < maxIndex;
    }

    public void redoLast() {
        if(canRedo()) {
            executables[currentActionIndex].apply();
            currentActionIndex++;
        }
    }

    private void freeBufferSpace() {
        currentActionIndex = BUFFER_MAX_SIZE;
        maxIndex = BUFFER_MAX_SIZE;
        for (int i = 0; i < BUFFER_MAX_SIZE; i++) {
            executables[i] = executables[maxIndex + i];
        }
    }
}
