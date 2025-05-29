package ui.main_panes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import logic.video_processing.queue.ProcessingQueue;
import ui.BaseController;
import ui.EditWindowController;
import ui.LoadController;
import ui.PaneController;

public class LoadPane extends ContentPane {
    PaneController paneController;

    public LoadPane(ProcessingQueue processingQueue) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx_screens/load.fxml"));
            paneController = new LoadController(processingQueue);
            loader.setController(paneController);
            Node node = loader.load();
            this.getChildren().setAll(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public PaneController getController() {
        return paneController;
    }
}
