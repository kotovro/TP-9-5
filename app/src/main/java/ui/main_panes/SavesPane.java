package ui.main_panes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import ui.PaneController;

public class SavesPane extends ContentPane {
    PaneController paneController;
    public SavesPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx_screens/saves.fxml"));
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
