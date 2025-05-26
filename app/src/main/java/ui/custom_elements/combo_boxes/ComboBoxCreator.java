package ui.custom_elements.combo_boxes;

import javafx.scene.control.ComboBox;
import logic.general.Speaker;
import logic.persistence.DBManager;
import logic.video_processing.vosk.analiseDTO.RawTranscript;
import ui.BaseController;

import java.util.ArrayList;
import java.util.List;

public class ComboBoxCreator {
    private List<ComboBoxGroup> comboBoxGroupList = new ArrayList<ComboBoxGroup>();
    private List<Speaker> speakers;
    private RawTranscript rawTranscript;
    private BaseController baseController;

    public ComboBoxCreator(RawTranscript rawTranscript, List<Speaker> speakers, BaseController baseController) {
        this.baseController = baseController;
        this.speakers = speakers;
        this.rawTranscript = rawTranscript;

        for (int i = 0; i < rawTranscript.getSpeakerCount(); i++) {
            comboBoxGroupList.addLast(new ComboBoxGroup(speakers, i, baseController));
        }
    }

    public ComboBox<Speaker> createComboBox(int orderNumber) {
        int groupID = rawTranscript.getGroupID(orderNumber);
        if (groupID != -1) {
            return comboBoxGroupList.get(groupID).createComboBox();
        } else {
            return createNewComboBox();
        }
    }

    public ComboBox<Speaker> createNewComboBox() {
        return new SearchableComboBox(speakers, "", baseController);
    }
}