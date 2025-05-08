package ui;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
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
        menuButton.setOnAction(event -> toggleMenu());
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
