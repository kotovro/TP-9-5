package ui.main_panes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import ui.BaseController;
import ui.PaneController;

public class DialogEdit extends Pane {
    //private BaseController BaseController;
    public DialogEdit() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx_screens/DialogEdit.fxml"));
            Node node = loader.load();
            this.getChildren().setAll(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
