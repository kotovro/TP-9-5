package ui.main_panes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import ui.PaneController;

public class MainPane extends ContentPane {
    PaneController paneController;

    public MainPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx_screens/mainWindow.fxml"));
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
