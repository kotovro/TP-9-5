package ui.custom_elements;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import logic.general.Speaker;
import logic.general.Transcript;
import logic.persistence.DBManager;
import logic.video_processing.vosk.analiseDTO.RawTranscript;
import ui.BaseController;
import ui.custom_elements.combo_boxes.ComboBoxCreator;
import ui.custom_elements.combo_boxes.SearchableComboBox;

import java.util.Date;
import java.util.List;

public class RawTranscriptDisplayer extends BaseDisplayer {
    private static final String PREFIX = "Без названия ";
    private final RawTranscript rawTranscript;
    private final BaseController baseController;

    public RawTranscriptDisplayer(RawTranscript rawTranscript, List<Speaker> speakers, BaseController baseController) {
        super(PREFIX + (rawTranscript.getID() + 1) + ".tr", speakers);
        this.rawTranscript = rawTranscript;
        this.baseController = baseController;
        initTextArea();
    }

    @Override
    protected void initTextArea() {
        ComboBoxCreator comboBoxCreator = new ComboBoxCreator(rawTranscript);
        for (int i = 0; i < rawTranscript.getPhraseCount(); i++) {
            textAreaContainer.getChildren().add(formReplicaView(rawTranscript, comboBoxCreator, i));
        }
    }

    @Override
    protected void initButtonsActions(Button save, Button saveAs) {
        save.setDisable(true);
        saveAs.setOnAction(e -> {
            baseController.
        });
    }

    private BasePane formReplicaView(RawTranscript rawTranscript, ComboBoxCreator comboBoxCreator, int index) {
        ComboBox<Speaker> comboBox = comboBoxCreator.createComboBox(index);
        TextArea textArea = initTextArea(rawTranscript.getPhrase(index));
        BasePane basePane = new TimeCodeBasePane(comboBox, textArea, rawTranscript.getTimeCode(index), this);
        VBox.setMargin(basePane, basePaneInsets);
        return basePane;
    }

    private void saveAsToDB(String name) {
        Transcript transcript = new Transcript(name, new Date());
        for (Node node : textAreaContainer.getChildren()) {
            BasePane pane = (BasePane) node;
            transcript.addReplica(pane.getReplica());
        }
        DBManager.getTranscriptDao().addTranscript(transcript);
    }
}
