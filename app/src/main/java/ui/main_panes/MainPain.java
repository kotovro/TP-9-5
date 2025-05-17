package ui.main_panes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import ui.PaneController;

public class MainPain extends ContentPane {
    PaneController paneController;

    public MainPain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx_screens/" + "mainWindow.fxml"));
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
