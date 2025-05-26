package ui.main_panes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import logic.general.Transcript;
import logic.video_processing.queue.ProcessingQueue;
import ui.*;

import java.util.Date;

public class SavesPane extends ContentPane {
    PaneController paneController;
    public SavesPane(EditWindowController editWindowController, ProcessingQueue processingQueue, BaseController baseController) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx_screens/saves.fxml"));
            loader.setController(new SavesController(editWindowController, processingQueue, baseController));
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
