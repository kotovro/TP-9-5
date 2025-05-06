package ui;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
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

    @FXML
    private Button loadButton;

    private final int MENU_WIDTH = 200;
    private boolean isMenuOpen = false;

    @FXML
    private VBox sideMenu;

    @FXML
    private Button menuButton;

    private void toggleMenu() {
        isMenuOpen = !isMenuOpen;

        TranslateTransition animation = new TranslateTransition(Duration.millis(300), sideMenu);
        animation.setFromX(isMenuOpen ? -MENU_WIDTH : 0);
        animation.setToX(isMenuOpen ? 0 : -MENU_WIDTH);
        animation.play();
    }

    // Обработчики для пунктов меню
    @FXML
    private void handleMainClick() {
        // переход на главную
    }

    @FXML
    private void handleSavingsClick() {
        // переход на сохраненные файлы
    }

    @FXML
    private void handleEditClick() {
        // переход на обработать видео
    }

    @FXML
    private void handleExitClick() {
        System.exit(0);
    }

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
        menuButton.setOnAction(event -> toggleMenu());
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

                    Stage stage = (Stage) confirmButton.getScene().getWindow(); // Получаем текущую сцену
                    stage.setResizable(false);
                    Scene secondScene = new Scene(loader.load(), 1137, 778);
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
