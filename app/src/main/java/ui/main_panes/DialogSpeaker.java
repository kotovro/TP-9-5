package ui.main_panes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import ui.DialogSaveController;
import ui.DialogSpeakerController;
import ui.EditWindowController;
import ui.PaneController;

public class DialogSpeaker extends Pane {
    public DialogSpeaker() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx_screens/DialogSpeaker.fxml"));
            loader.setController(new DialogSpeakerController());
            Node node = loader.load();
            this.getChildren().setAll(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

