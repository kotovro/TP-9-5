package ui.main_panes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import ui.PaneController;

public class DialogRecord extends Pane {
    public DialogRecord() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx_screens/DialogRecord.fxml"));
            Node node = loader.load();
            this.getChildren().setAll(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
