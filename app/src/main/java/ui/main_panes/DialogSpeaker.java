package ui.main_panes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import logic.general.Speaker;
import ui.*;

import java.util.List;

public class DialogSpeaker extends Pane {
    public DialogSpeaker(BaseController baseController, List<Speaker> speakers) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx_screens/DialogSpeaker.fxml"));
            loader.setController(new DialogSpeakerController(baseController, speakers));
            Node node = loader.load();
            this.getChildren().setAll(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

