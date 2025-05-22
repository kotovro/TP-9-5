package ui.main_panes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import logic.general.Transcript;
import ui.PaneController;

import java.util.Date;

public class SavesPane extends ContentPane {
    PaneController paneController;
    public SavesPane() {
        try {
            Transcript t = new Transcript("nenfieji", new Date());
            Transcript t1 = new Transcript("cfnqekaifhnwiowfhwpdfjqpo", new Date());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx_screens/saves.fxml"));
            Node node = loader.load();
            this.getChildren().setAll(node);

            paneController = loader.getController();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public PaneController getController() {
        return paneController;
    }
}
