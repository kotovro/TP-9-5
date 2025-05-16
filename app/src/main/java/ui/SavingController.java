package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.persistence.DBManager;
import logic.persistence.exception.UniqueTranscriptNameViolationException;

public class SavingController {
    public TextField textField;
    @FXML
    private Button Save;

    @FXML
    private Pane errorPane;

    @FXML
    public void initialize() {
        textField.setText(GlobalState.transcript.getName());
    }

    @FXML
    protected void onFileButtonClick(ActionEvent event) {
        errorPane.setVisible(false);
        if (textField.getText().isEmpty()) return;
        try {
            DBManager.getTranscriptDao().addTranscript(GlobalState.transcript);
        } catch (UniqueTranscriptNameViolationException e) {
            errorPane.setVisible(true);
            return;
        }
        Stage stage = (Stage) textField.getScene().getWindow();
        LoadStenogrammApp.setStage(stage);
    }
}
