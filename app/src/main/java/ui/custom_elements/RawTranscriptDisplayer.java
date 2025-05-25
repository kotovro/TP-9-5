package ui.custom_elements;

import javafx.scene.Node;
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
import ui.custom_elements.combo_boxes.ComboBoxCreator;
import ui.custom_elements.combo_boxes.SearchableComboBox;

import java.util.Date;
import java.util.List;

public class RawTranscriptDisplayer extends BaseDisplayer{
    private static final String PREFIX = "Без названия ";
    private final RawTranscript rawTranscript;

    public RawTranscriptDisplayer(RawTranscript rawTranscript, List<Speaker> speakers) {
        super(PREFIX + (rawTranscript.getID() + 1) + ".tr", speakers);
        this.rawTranscript = rawTranscript;
        initTextArea();
    }

    protected void saveAsToDB(String name) {
        Transcript transcript = new Transcript(name, new Date());
        for (Node node : textAreaContainer.getChildren()) {
            BasePane pane = (BasePane) node;
            transcript.addReplica(pane.getReplica());
        }
        DBManager.getTranscriptDao().addTranscript(transcript);
    }

    @Override
    protected void initTextArea() {
        ComboBoxCreator comboBoxCreator = new ComboBoxCreator(rawTranscript);
        for (int i = 0; i < rawTranscript.getPhraseCount(); i++) {
            textAreaContainer.getChildren().add(formReplicaView(rawTranscript, comboBoxCreator, i));
        }
    }

    private BasePane formReplicaView(RawTranscript rawTranscript, ComboBoxCreator comboBoxCreator, int index) {
        ComboBox<Speaker> comboBox = comboBoxCreator.createComboBox(index);
        TextArea textArea = initTextArea(rawTranscript.getPhrase(index));
        BasePane basePane = new BasePane(comboBox, textArea, this);
        VBox.setMargin(basePane, basePaneInsets);
        return basePane;
    }
}
