package ui.main_panes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import logic.general.Speaker;
import ui.*;

import java.util.List;

public class DialogSpeaker extends Pane {
    private final DialogSpeakerController controller;
    public DialogSpeaker(BaseController baseController, List<Speaker> speakers) {
        controller = new DialogSpeakerController(baseController, speakers);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx_screens/DialogSpeaker.fxml"));
            loader.setController(controller);
            Node node = loader.load();
            this.getChildren().setAll(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setName(String name) {
        controller.setName(name);
    }
}

