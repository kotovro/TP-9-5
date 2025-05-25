package ui.main_panes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import ui.DialogSaveController;
import ui.EditWindowController;
import ui.PaneController;

public class DialogSave extends Pane {
    public DialogSave() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx_screens/DialogSave.fxml"));
            loader.setController(new DialogSaveController());
            Node node = loader.load();
            this.getChildren().setAll(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
