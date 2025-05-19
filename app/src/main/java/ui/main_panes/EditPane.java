package ui.main_panes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import ui.BaseController;
import ui.EditWindowController;
import ui.PaneController;

import static ui.GlobalState.transcript;

public class EditPane extends ContentPane {
    PaneController paneController;
    BaseController bc;


    public EditPane(BaseController bc) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx_screens/EditView.fxml"));
            loader.setController(new EditWindowController(transcript, bc));
            Node node = loader.load();
            this.getChildren().setAll(node);

            paneController = loader.getController();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public PaneController getController() {
        return paneController;
    }
}
