package ui.main_panes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import ui.BaseController;
import ui.NoEditDialogController;

public class DialogNoEdit extends Pane {
    public DialogNoEdit(BaseController baseController) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx_screens/DialogEdit.fxml"));
            loader.setController(new NoEditDialogController(baseController));
            Node node = loader.load();
            this.getChildren().setAll(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
