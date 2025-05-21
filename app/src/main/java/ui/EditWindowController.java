package ui;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import logic.general.Replica;
import logic.general.Speaker;
import logic.general.Transcript;

import java.util.ArrayList;
import java.util.List;

public class EditWindowController implements PaneController{
    @FXML
    private ScrollPane replicas;
    @FXML
    private Pane tabs;
    private final Transcript transcript;
    private List<Speaker> speakers = new ArrayList<>();
    public Stenogramm active = null;
    public List<Stenogramm> stenogramms = new ArrayList<>(); // скорее всего создаваться будет не здесь, тогда лучше передавать
    // не активную стенограмму, а этот лист, чтобы если он пустой вызывать диалоговое окно.
    BaseController bc;

    @FXML
    public void initialize() {
        Stenogramm st = new Stenogramm(transcript);
        Stenogramm st2 = new Stenogramm(transcript);
        active = st;
        stenogramms.add(st);
        stenogramms.add(st2);
        active.fillPane(replicas);
        loadTabs();

    }

    public void loadTabs() {
        System.out.println("Active: " + active);
        tabs.getChildren().clear();
        HBox tabBase = new HBox();
        for (Stenogramm stenogramm : new ArrayList<>(stenogramms)) {
            Tabs tab = new Tabs(stenogramm);
            tabBase.getChildren().add(tab);
            if (!stenogramm.equals(active)) {
                tabBase.setMargin(tab, new Insets(10, 10, 10, 10));
                tab.setStyle("-fx-background-color: #CDCEFF; -fx-background-radius: 11");
                tab.name.setStyle("-fx-font-family: 'Manrope'; -fx-font-size: 14; -fx-text-fill: black;");
                tab.setMargin(tab.name, new Insets(14, 6, 6, 20));
                tab.setMargin(tab.close, new Insets(8, 6, 6, 6));
            }
            else {
                tab.setStyle("-fx-background-color: #0B0F85; -fx-background-radius: 11 11 0 0");
                tab.name.setStyle("-fx-font-family: 'Manrope'; -fx-font-size: 16; -fx-text-fill: white;");
                tab.setMargin(tab.name, new Insets(16, 6, 6, 20));
                tab.setMargin(tab.close, new Insets(12, 6, 6, 6));
                tabBase.setMargin(tab, new Insets(10, 10, 0, 10));
            }
            tab.setOnMouseClicked(event -> {
                if (event.getTarget() == tab.close) {
                    return;
                }
                active = stenogramm;
                active.fillPane(replicas);
                loadTabs();
            });
            tab.close.setOnMouseClicked(event -> {
                boolean wasActive = stenogramm.equals(active);
                stenogramms.remove(stenogramm);

                if (wasActive) {
                    active = stenogramms.isEmpty() ? null : stenogramms.get(0);
                }

                if (active != null) {
                    active.fillPane(replicas);
                } else {
                    replicas.setContent(null);
                    load();
                }
                loadTabs();
            });
        }
        tabs.getChildren().add(tabBase);
    }

    private void handleTabClose(Stenogramm stenogramm) {

    }

    public EditWindowController(Transcript transcript, BaseController bc) {
        this.bc = bc;
        this.transcript = transcript;
    }

    @Override
    public boolean load() {
        if (active == null) {
            bc.loaddialog();
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
}
