package ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import logic.general.Transcript;
import ui.custom_elements.CardView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoadStenogrammController {

    @FXML
    private FlowPane cardPane;

    @FXML
    private Button confirmButton;

    private CardView selectedCard;

    @FXML
    private Button loadButton;

    @FXML
    private void loadFromVideo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx_screens/downloading.fxml"));
            Scene newScene = new Scene(loader.load());
            Stage newStage = new Stage();
            newStage.setScene(newScene);
            newStage.setTitle("Загрузка стенограммы");
            newStage.show();

            Stage currentStage = (Stage) loadButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void initialize() {
        confirmButton.setDisable(true);
        cardPane.setHgap(15);
        cardPane.setVgap(15);
        Transcript transcript = new Transcript("loma", new Date());
        List<CardView> cards = new ArrayList<>();

        for (int i = 1; i <= 5; i++) { // типо количество стенограмм
            CardView card = new CardView(transcript);
            cards.add(card);

            card.setOnSelected(() -> {
                if (selectedCard != null) {
                    selectedCard.setSelected(false);
                }
                card.setSelected(true);
                selectedCard = card;
                confirmButton.setDisable(false);
            });

            cardPane.getChildren().add(card);
        }



        confirmButton.setOnAction(e -> {
            if (selectedCard == null) return;

            try {
                confirmButton.setDisable(true);
                Thread.sleep(500);
                Scene secondScene = EditWindow.getScene(selectedCard.getTranscript());
                Stage stage = (Stage) confirmButton.getScene().getWindow(); // Получаем текущую сцену
                stage.setScene(secondScene);
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        cardPane.getStyleClass().add("flowpane-transparent");
    }
}
