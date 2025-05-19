package ui;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import logic.general.Speaker;
import logic.general.Transcript;

import java.util.ArrayList;
import java.util.List;

public class EditWindowController implements PaneController{
    @FXML
    private Pane replicas;
    private final Transcript transcript;
    private List<Speaker> speakers = new ArrayList<>();
    public Stenogramm active = null;
    BaseController bc;

    @FXML
    public void initialize() {
        Stenogramm st = new Stenogramm(transcript);
        st.fillPane(replicas);
    }

    public EditWindowController(Transcript transcript, BaseController bc) {
        this.bc = bc;
        this.transcript = transcript;
    }
    @Override
    public void stopAnimation() {

    }

    @Override
    public void startAnimation() {

    }

    @Override
    public void load() {
        if (active == null) {
            bc.loaddialog();
        }
    }

}
