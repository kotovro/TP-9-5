package ui.main_panes;

import javafx.scene.layout.Pane;
import ui.PaneController;

public abstract class ContentPane extends Pane {
    public abstract PaneController getController();
}
