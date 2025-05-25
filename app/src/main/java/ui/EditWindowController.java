package ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import logic.general.*;
import logic.persistence.DBManager;
import logic.video_processing.queue.listeners.SummarizeListener;
import logic.video_processing.queue.listeners.TranscriptListener;
import logic.video_processing.vosk.analiseDTO.RawTranscript;
import ui.custom_elements.ProtocolDisplayer;
import ui.custom_elements.RawTranscriptDisplayer;
import ui.custom_elements.Tab;
import ui.custom_elements.TranscriptDisplayer;

import java.util.ArrayList;
import java.util.List;

public class EditWindowController implements PaneController, TranscriptListener, SummarizeListener  {
    @FXML
    private ScrollPane replicas;

    @FXML
    private Pane pane;

    @FXML
    private Pane paneReplicas;
    @FXML
    private ScrollPane tabPane;

    private static final String STYLE = BaseController.class.getResource("/styles/download.css").toExternalForm();

    private HBox tabRow = new HBox();
    private final List<Speaker> speakers = DBManager.getSpeakerDao().getAllSpeakers();;
    public Tab active = null;
    public List<Tab> tabs = new ArrayList<>();
    BaseController baseController;

    public EditWindowController(BaseController baseController) {
        this.baseController = baseController;
    }

    @FXML
    public void initialize() {
        tabPane.getStyleClass().add(STYLE);
        replicas.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        tabPane.setContent(tabRow);
        initSize();
    }

    public void setActive(Tab active) {
        if (this.active != null) {
            this.active.setInactive();
        }

        if (this.active != null && this.active.getTranscriptDisplayer().delete != null) {
            paneReplicas.getChildren().remove(this.active.getTranscriptDisplayer().delete);
        }

        if (this.active != null && this.active.getTranscriptDisplayer().file != null) {
            paneReplicas.getChildren().remove(this.active.getTranscriptDisplayer().file);
        }

        if (this.active != null && this.active.getTranscriptDisplayer().filePane != null) {
            paneReplicas.getChildren().remove(this.active.getTranscriptDisplayer().filePane);
        }

        if (active != null) {
            active.setActive();
            active.getTranscriptDisplayer().setupPane(replicas);
            active.getTranscriptDisplayer().setupDelete(paneReplicas);
            active.getTranscriptDisplayer().setupMenu(paneReplicas);
            active.getTranscriptDisplayer().file.setOnAction(e ->
            {
                active.getTranscriptDisplayer().isOpen = !active.getTranscriptDisplayer().isOpen;
                active.getTranscriptDisplayer().filePane.setVisible(active.getTranscriptDisplayer().isOpen);
                pane.toFront();
                //pane.setMouseTransparent(active.getTranscriptDisplayer().isOpen);
            });
        }


        this.active = active;
    }

    public void addTab(Tab tab) {
        tabs.add(tab);
        tabRow.getChildren().add(tab);
        if (active == null) {
            setActive(tab);
        }
    }

    public void removeTab(Tab tab) {
        tabs.remove(tab);
        tabRow.getChildren().remove(tab);
        if (tab.equals(active)) {
            setActive(!tabs.isEmpty() ? tabs.getFirst() : null);
        }
        if (active != null) {
            active.getTranscriptDisplayer().setupPane(replicas);
            active.getTranscriptDisplayer().setupDelete(paneReplicas);
        } else {
            replicas.setContent(null);
            load();
        }
    }

    @Override
    public boolean load() {
        if (active == null) {
            baseController.loadEditDialog();
            return false;
        }
        return true;
    }

    @Override
    public void stopAnimation() {

    }

    @Override
    public void startAnimation() {

    }

    public void addTranscript(Transcript transcript) {
        Platform.runLater(() -> {
            addTab(new Tab(new TranscriptDisplayer(transcript, speakers), this));
        });
    }

    public void addProtocol(MeetingMaterials meetingMaterials) {
        Platform.runLater(() -> {
            addTab(new Tab(new ProtocolDisplayer(meetingMaterials, speakers), this));
        });
    }

    private void initSize() {
        HBox tmp = new HBox();
        HBox.setMargin(tmp, new Insets(0, 0, 10, 0));
        tmp.setPrefHeight(59);
        tabRow.getChildren().add(tmp);
    }

    @Override
    public void onResultReady(RawTranscript rawTranscript) {
        Platform.runLater(() -> {
            addTab(new Tab(new RawTranscriptDisplayer(rawTranscript, speakers), this));
        });
    }

    @Override
    public void onResultReady(MeetingMaterials meetingMaterials) {
        Platform.runLater(() -> {
            addTab(new Tab(new ProtocolDisplayer(meetingMaterials, speakers), this));
        });
    }
}
