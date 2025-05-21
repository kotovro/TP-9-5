package ui;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class SavesController implements PaneController{

    @FXML
    private Pane saves;

    @FXML
    public void initialize(){
        VBox v = new VBox();
        // for stenogramms in base:
        StAndPrBuilder sp = new StAndPrBuilder();
        VBox.setMargin( sp,  new Insets(30, 10, 0, 30));
        v.getChildren().add(sp);
        saves.getChildren().add(v);
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
