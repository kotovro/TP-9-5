package ui;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import logic.general.*;
import logic.persistence.DBManager;
import logic.persistence.dao.ProtocolDao;
import logic.video_processing.queue.ProcessingQueue;
import ui.custom_elements.TranscriptDisplayer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class SavesController implements PaneController{
    @FXML
    private ScrollPane saves;

    private static final String STYLE = TranscriptDisplayer.class.getResource("/styles/load.css").toExternalForm();

    private EditWindowController editWindowController;
    private ProcessingQueue processingQueue;
    private BaseController baseController;
    private VBox content = new VBox();
    private List<MeetingMaterials> processedMaterials = new ArrayList<>();

    public SavesController(EditWindowController editWindowController, ProcessingQueue processingQueue, BaseController baseController) {
        this.editWindowController = editWindowController;
        this.processingQueue = processingQueue;
        this.baseController = baseController;
    }

    @FXML
    public void initialize() {
        saves.getStyleClass().add("saves-scroll-pane");
        updateContent();
        saves.setContent(content);
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

    public void updateContent() {
        content.getChildren().clear();
        List<MeetingMaterials> meetingMaterials = DBManager.getMeetingMaterialsDao().getAllMeetingMaterials();
        for (MeetingMaterials materials : meetingMaterials) {
            StAndPrBuilder stAndPrBuilder = new StAndPrBuilder(materials, editWindowController, processingQueue,
                    baseController, this);
            VBox.setMargin(stAndPrBuilder, new Insets(20, 0, 10, 15));
            content.getChildren().add(stAndPrBuilder);
        }
    }

    public boolean isProcessing(MeetingMaterials materials) {
        return processedMaterials.contains(materials);
    }

    public void addToProcessing(MeetingMaterials materials) {
        processedMaterials.add(materials);
    }

    @Override
    public void stopAnimation() {

    }

    @Override
    public void startAnimation() {

    }

    @Override
    public boolean load() {
        updateContent();
        return true;
    }
}
