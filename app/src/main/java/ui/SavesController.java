package ui;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import logic.general.Protocol;
import logic.general.Task;
import logic.general.Transcript;
import logic.persistence.DBManager;
import logic.persistence.dao.ProtocolDao;
import logic.video_processing.queue.ProcessingQueue;
import ui.custom_elements.TranscriptDisplayer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SavesController implements PaneController{
    @FXML
    private ScrollPane saves;

    private static final String STYLE = TranscriptDisplayer.class.getResource("/styles/load.css").toExternalForm();

    private EditWindowController editWindowController;
    private ProcessingQueue processingQueue;

    public SavesController(EditWindowController editWindowController, ProcessingQueue processingQueue) {
        this.editWindowController = editWindowController;
        this.processingQueue = processingQueue;
    }

    @FXML
    public void initialize() {
        saves.getStyleClass().add("saves-scroll-pane");
        VBox v = new VBox();
//        DBManager.getProtocolDao().
        for (Transcript transcript : DBManager.getTranscriptDao().getTranscripts()) {
//            StAndPrBuilder stAndPrBuilder = new StAndPrBuilder(transcript, );
//            VBox.setMargin(stAndPrBuilder, new Insets(20, 0, 10, 15));
//            v.getChildren().add(stAndPrBuilder);
        }
        for (int i = 0; i < 4; i++){
            StAndPrBuilder sp = new StAndPrBuilder(new Transcript("nenfieji", new Date()), null, new ArrayList<>(),
                    editWindowController, processingQueue);
            VBox.setMargin(sp, new Insets(20, 0, 10, 15));
            v.getChildren().add(sp);
        }
        saves.setContent(v);
        if (saves.getScene() != null) {
            saves.getScene().getStylesheets().add(STYLE);
        } else {
            saves.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    newScene.getStylesheets().add(STYLE);
                }
            });
        }
    }

    @Override
    public void stopAnimation() {

    }

    @Override
    public void startAnimation() {

    }

    @Override
    public boolean load() {
        return true;
    }
}
