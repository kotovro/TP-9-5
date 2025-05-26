package ui.main_panes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import logic.general.Replica;
import logic.general.Speaker;
import logic.general.Transcript;
import logic.video_processing.queue.ProcessingQueue;
import logic.video_processing.queue.listeners.TranscriptListener;
import logic.video_processing.vosk.analiseDTO.RawTranscript;
import ui.BaseController;
import ui.EditWindowController;
import ui.PaneController;

import java.util.Date;
import java.util.List;


public class EditPane extends ContentPane {
    EditWindowController paneController;
    BaseController bc;

    public EditPane(BaseController bc, ProcessingQueue processingQueue, List<Speaker> speakers) {
        try {
            this.bc = bc;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx_screens/EditView.fxml"));
            EditWindowController controller = new EditWindowController(bc, speakers);
            loader.setController(controller);
            processingQueue.setTranscriptListener(controller);
            processingQueue.setSummarizeListener(controller);
            Node node = loader.load();
            this.getChildren().setAll(node);

            paneController = loader.getController();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public EditWindowController getController() {
        return paneController;
    }
}
