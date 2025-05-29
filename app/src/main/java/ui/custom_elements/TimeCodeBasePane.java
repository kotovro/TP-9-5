package ui.custom_elements;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import logic.general.Replica;
import logic.general.Speaker;
import logic.utils.TimeCode;
import logic.utils.TimeFormatter;

public class TimeCodeBasePane extends BasePane {
    private TimeCodeBox timeCodeBox;

    public TimeCodeBasePane(ComboBox<Speaker> combobox, TextArea textarea, TimeCode timeCode, BaseDisplayer baseDisplayer) {
        super(combobox, textarea, baseDisplayer);
        timeCodeBox = new TimeCodeBox(timeCode);
        getChildren().add(timeCodeBox);
        addButton.setLayoutX(520);
        addPane.setLayoutX(570);
    }

    @Override
    public Replica getReplica() {
        Speaker selected = combobox.getSelectionModel().getSelectedItem();
        return new Replica(textarea.getText(), selected == null ? combobox.getItems().get(1) : selected,
                TimeFormatter.toSeconds(timeCodeBox.getTimeCode()));
    }
}
