package ui;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import logic.general.Speaker;
import logic.general.Replica;
import logic.general.Transcript;
import logic.persistence.DBManager;

import java.util.Date;

public class SavesController implements PaneController{

    @FXML
    private ScrollPane saves;

    private static final String STYLE = TranscriptDisplayer.class.getResource("/styles/load.css").toExternalForm();

    @FXML
    public void initialize(){
        saves.getStyleClass().add("saves-scroll-pane");
        VBox v = new VBox();
        //for (Transcript transcript : DBManager.getTranscriptDao().getTranscripts()) {
        for (int i = 0; i < 4; i++){
            StAndPrBuilder sp = new StAndPrBuilder(new Transcript("nenfieji", new Date()));
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
        //}
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
