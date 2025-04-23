package ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import logic.general.Transcript;


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

    public void initialize() {
        confirmButton.setDisable(true);
        cardPane.setHgap(15);
        cardPane.setVgap(15);
        Transcript t = new Transcript("loma", new Date());
        Date date = t.getDate();
        String name = t.getName();
        List<CardView> cards = new ArrayList<>();

        for (int i = 1; i <= 5; i++) { // типо количество стенограмм
            CardView card = new CardView(name, "date");
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
            if (selectedCard != null) {
                try {
                    // Здесь можешь сохранить selectedCard.getnameText() или что тебе там вообще нужно
                    confirmButton.setDisable(true);
                    Thread.sleep(5000);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx_screens/EditView.fxml"));
                    Scene secondScene = new Scene(loader.load(), 600, 400);
                    Stage stage = (Stage) confirmButton.getScene().getWindow(); // Получаем текущую сцену
                    stage.setScene(secondScene);
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                //f,e;f,
            }
        });
        cardPane.getStyleClass().add("flowpane-transparent");
    }
}
