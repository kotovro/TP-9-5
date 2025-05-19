package ui.custom_elements;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import logic.general.Speaker;

import java.util.ArrayList;
import java.util.List;

public class SyncedComboBoxManager {
    private final List<ComboBox<Speaker>> comboBoxes = new ArrayList<>();
    private final ObservableList<Speaker> speakers;

    public SyncedComboBoxManager(List<Speaker> speakers, Speaker defaultSpeaker) {
        this.speakers = FXCollections.observableArrayList(speakers);
    }

    public ComboBox<Speaker> createComboBox() {
        SearchableDropdownComboBox comboBox = new SearchableDropdownComboBox(speakers, "Speaker 1");
        comboBoxes.add(comboBox);

        comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Speaker>() {
            @Override
            public void changed(ObservableValue<? extends Speaker> observableValue, Speaker speaker, Speaker newVal) {
                if (newVal != null) {
                    comboBoxes.forEach(cb -> {
                        if (cb != comboBox) {
                            cb.getSelectionModel().select(newVal);
                        }
                    });
                }
                comboBox.getSelectionModel().selectedItemProperty().removeListener(this);
                comboBox.setDefaultText("");
            }
        });
        return comboBox;
    }
}