package ui.custom_elements;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import logic.general.Speaker;
import logic.video_processing.vosk.analiseDTO.RawTranscript;
import ui.custom_elements.combo_boxes.ComboBoxCreator;

import java.util.List;

public class RawTranscriptDisplayer extends BaseDisplayer{
    private static final String PREFIX = "Без названия ";
    private final RawTranscript transcript;

    public RawTranscriptDisplayer(RawTranscript transcript, List<Speaker> speakers) {
        super(PREFIX + (transcript.getID() + 1), speakers);
        this.transcript = transcript;
        initTextArea();
    }

    @Override
    public void saveToDB() {

    }

    @Override
    protected void initTextArea() {
        ComboBoxCreator comboBoxCreator = new ComboBoxCreator(transcript);
        for (int i = 0; i < transcript.getPhraseCount(); i++) {
            textAreaContainer.getChildren().add(formReplicaView(transcript, comboBoxCreator, i));
        }
    }

    private BasePane formReplicaView(RawTranscript rawTranscript, ComboBoxCreator comboBoxCreator, int index) {
        ComboBox<Speaker> comboBox = comboBoxCreator.createComboBox(index);
        TextArea textArea = initTextArea(rawTranscript.getPhrase(index));
        ImageView deleteButton = new ImageView();
        BasePane basePane = new BasePane(comboBox, textArea, deleteButton);
        VBox.setMargin(basePane, basePaneInsets);
        return basePane;
    }
}
