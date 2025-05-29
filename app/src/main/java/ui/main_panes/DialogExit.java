package ui.main_panes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import ui.BaseController;
import ui.ExitDialogController;
import ui.PaneController;

public class DialogExit extends Pane {
    public DialogExit(BaseController controller) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx_screens/DialogExit.fxml"));
            loader.setController(new ExitDialogController(controller));
            Node node = loader.load();
            this.getChildren().setAll(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
