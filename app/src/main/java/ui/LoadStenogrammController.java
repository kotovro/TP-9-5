package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import logic.general.Transcript;
import logic.persistence.DBManager;
import ui.custom_elements.CardView;

public class LoadStenogrammController {

    @FXML
    private FlowPane cardPane;

    @FXML
    private Button confirmButton;

    private CardView selectedCard;

    @FXML
    private Button loadButton;

    @FXML
    public Button deleteButton;

    @FXML
    private void loadFromVideo() {
        try {
            Stage stage = (Stage) confirmButton.getScene().getWindow();
            DownloadingApp.setStage(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void initialize() {
        confirmButton.setDisable(true);
        cardPane.setHgap(15);
        cardPane.setVgap(15);

        for (Transcript transcript : DBManager.getTranscriptDao().getTranscripts()) {
            CardView card = new CardView(transcript);

            card.setOnSelected(() -> {
                if (selectedCard != null) {
                    selectedCard.setSelected(false);
                }
                card.setSelected(true);
                selectedCard = card;
                confirmButton.setDisable(false);
                deleteButton.setDisable(false);
            });

            cardPane.getChildren().add(card);
        }
        initDeleteButton();

        confirmButton.setOnAction(e -> {
            if (selectedCard == null) return;
            confirmButton.setDisable(true);
            deleteButton.setDisable(true);
            Stage stage = (Stage) confirmButton.getScene().getWindow();
            EditWindow.setStage(stage, selectedCard.getTranscript());
        });
        cardPane.getStyleClass().add("flowpane-transparent");
    }

    private void initDeleteButton() {
        deleteButton.setDisable(true);
        deleteButton.setOnAction(e -> {
            if (selectedCard == null) return;
            confirmButton.setDisable(true);
            deleteButton.setDisable(true);
            selectedCard.setSelected(false);
            DBManager.getTranscriptDao().deleteTranscript(selectedCard.getTranscript());
            cardPane.getChildren().remove(selectedCard);
            selectedCard = null;
        });
    }
}
