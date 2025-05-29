package ui.main_panes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import ui.BaseController;
import ui.DialogSaveController;
import ui.custom_elements.SaveAsProvider;

public class DialogSave extends Pane {
    private DialogSaveController controller;
    public DialogSave(BaseController baseController) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx_screens/DialogSave.fxml"));
            DialogSaveController controller = new DialogSaveController(baseController);
            this.controller = controller;
            loader.setController(controller);
            Node node = loader.load();
            this.getChildren().setAll(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSaveAsProvider(SaveAsProvider saveAsProvider) {
        controller.setSaveAsProvider(saveAsProvider);
    }
}
