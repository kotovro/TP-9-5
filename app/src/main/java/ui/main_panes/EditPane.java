package ui.main_panes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import logic.general.Replica;
import logic.general.Speaker;
import logic.general.Transcript;
import ui.BaseController;
import ui.EditWindowController;
import ui.PaneController;

import java.util.Date;


public class EditPane extends ContentPane {
    PaneController paneController;
    BaseController bc;


    public EditPane(BaseController bc) {
        try {

            Transcript t = new Transcript("nenfieji", new Date());
            Transcript t1 = new Transcript("cfnqekaifhnwiowfhwpdfjqpo", new Date());
            t.addReplica(new Replica("dwodkow", new Speaker("msowdow", null, 100)));
            t.addReplica(new Replica("dwodkow", new Speaker("msowdow", null, 100)));
            t1.addReplica(new Replica("dwodkow", new Speaker("msowdow", null, 100)));
            this.bc = bc;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx_screens/EditView.fxml"));
            loader.setController(new EditWindowController(t, bc));
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
