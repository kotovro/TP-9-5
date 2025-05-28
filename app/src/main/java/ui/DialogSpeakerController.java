package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import logic.general.Speaker;
import logic.persistence.DBManager;

import java.util.List;

public class DialogSpeakerController {
    @FXML
    private TextField textSpeaker;
    @FXML
    private Button addButton;

    private static final Image defaultImage = DBManager.getSpeakerDao().getSpeakerById(1).getImage();
    private BaseController baseController;
    private List<Speaker> speakers;

    public DialogSpeakerController(BaseController baseController, List<Speaker> speakers) {
        this.baseController = baseController;
        this.speakers = speakers;
    }

    public void initialize() {
        addButton.setOnAction(e -> {
            Speaker speaker = new Speaker(textSpeaker.getText(), defaultImage, -1);
            DBManager.getSpeakerDao().addSpeaker(speaker);
            speaker = DBManager.getSpeakerDao().getAllSpeakers().getLast();
            speakers.addLast(speaker);
            baseController.dialog.close();
        });
    }
}
