package ui.custom_elements.combo_boxes;

import javafx.scene.control.ComboBox;
import logic.general.Speaker;
import logic.persistence.DBManager;
import logic.video_processing.vosk.analiseDTO.RawTranscript;

import java.util.ArrayList;
import java.util.List;

public class ComboBoxCreator {
    private List<ComboBoxGroup> comboBoxGroupList = new ArrayList<ComboBoxGroup>();
    private List<Speaker> speakers;
    private RawTranscript rawTranscript;

    public ComboBoxCreator(RawTranscript rawTranscript) {
        speakers = DBManager.getSpeakerDao().getAllSpeakers();
        this.rawTranscript = rawTranscript;

        for (int i = 0; i < rawTranscript.getSpeakerCount(); i++) {
            comboBoxGroupList.addLast(new ComboBoxGroup(speakers, i));
        }
    }

    public ComboBox<Speaker> createComboBox(int orderNumber) {
        return comboBoxGroupList.get(rawTranscript.getGroupID(orderNumber)).createComboBox();
    }

    public ComboBox<Speaker> createNewComboBox() {
        return new SearchableComboBox(speakers, "");
    }
}