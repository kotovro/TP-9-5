package ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import logic.general.Speaker;
import logic.general.Transcript;
import logic.persistence.DBManager;

import java.util.ArrayList;
import java.util.List;

public class EditWindowController implements PaneController {
    @FXML
    private ScrollPane replicas;

    @FXML
    private Pane pane;

    @FXML
    private Pane paneReplicas;
    @FXML
    private ScrollPane tabPane;
    private HBox tabRow = new HBox();
    private final Transcript transcript;
    private final List<Speaker> speakers = DBManager.getSpeakerDao().getAllSpeakers();;
    public Tab active = null;
    public List<Tab> tabs = new ArrayList<>();
    BaseController bc;


    @FXML
    public void initialize() {
        tabPane.getStyleClass().add("tab-scroll-pane");
        replicas.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        tabPane.setContent(tabRow);
        initSize();
        Tab st = new Tab(new TranscriptDisplayer(transcript, speakers), this);
        addTab(new Tab(new TranscriptDisplayer(transcript, speakers), this));
        addTab(new Tab(new TranscriptDisplayer(transcript, speakers), this));
        addTab(new Tab(new TranscriptDisplayer(transcript, speakers), this));
        addTab(new Tab(new TranscriptDisplayer(transcript, speakers), this));
        addTab(new Tab(new TranscriptDisplayer(transcript, speakers), this));
        addTab(new Tab(new TranscriptDisplayer(transcript, speakers), this));
        addTab(new Tab(new TranscriptDisplayer(transcript, speakers), this));
        addTab(new Tab(new TranscriptDisplayer(transcript, speakers), this));
        addTab(new Tab(new TranscriptDisplayer(transcript, speakers), this));
        addTab(new Tab(new TranscriptDisplayer(transcript, speakers), this));
        addTab(new Tab(new TranscriptDisplayer(transcript, speakers), this));
        addTab(new Tab(new TranscriptDisplayer(transcript, speakers), this));
        addTab(new Tab(new TranscriptDisplayer(transcript, speakers), this));
        addTab(new Tab(new TranscriptDisplayer(transcript, speakers), this));
        addTab(st);
        setActive(st);
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
    }

    public void removeTab(Tab tab) {
        tabs.remove(tab);
        tabRow.getChildren().remove(tab);
        if (tab.equals(active)) {
            setActive(!tabs.isEmpty() ? tabs.getLast() : null);
        }
        if (active != null) {
            active.getTranscriptDisplayer().setupPane(replicas);
            active.getTranscriptDisplayer().setupDelete(paneReplicas);
        } else {
            replicas.setContent(null);
            load();
        }
    }

    public EditWindowController(Transcript transcript, BaseController bc) {
        this.bc = bc;
        this.transcript = transcript;
    }

    @Override
    public boolean load() {
        if (active == null) {
            bc.loadDialog();
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

    private void initSize() {
        HBox tmp = new HBox();
        HBox.setMargin(tmp, new Insets(0, 0, 10, 0));
        tmp.setPrefHeight(59);
        tabRow.getChildren().add(tmp);
    }
}
