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
import logic.persistence.DBManager;
import ui.custom_elements.CardView;

import java.util.Date;

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

    @FXML
    private Button main;

    @FXML
    private Button save;

    @FXML
    private Button change;


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
        Stage stage = (Stage) main.getScene().getWindow();
        MainWindow.setStage(stage);
    }

    @FXML
    private Button download;

    @FXML
    private void handleDownloadClick() {
        Stage stage = (Stage) download.getScene().getWindow();
        DownloadingApp.setStage(stage);
    }

    @FXML
    private void handleSavingsClick() {
        Stage stage = (Stage) save.getScene().getWindow();
        LoadStenogrammApp.setStage(stage);
    }

    @FXML
    private void handleEditClick() {
        if (GlobalState.transcript == null) {
            try {
                FXMLLoader loader = new FXMLLoader(EditController.class.getResource("/fx_screens/loadOptional.fxml"));
                Scene scene = new Scene(loader.load());
                scene.getStylesheets().add(getClass().getResource("/styles/dialog-style.css").toExternalForm());

                Stage dialog = new Stage();
                dialog.setResizable(false);
                dialog.initOwner(change.getScene().getWindow());
                dialog.setTitle("Выбор источника загрузки");
                dialog.setScene(scene);

                LoadOptionDialogController controller = loader.getController();
                Stage mainStage = (Stage) change.getScene().getWindow();
                controller.setMainStage(mainStage);

                controller.setLabelText("Сейчас не выбрано ничего для редактирования");

                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Stage stage = (Stage) change.getScene().getWindow();
            EditWindow.setStage(stage, GlobalState.transcript);
        }
    }

    @FXML
    private void handleExitClick() {
        System.exit(0);
    }

    @FXML
    public Button deleteButton;

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
            GlobalState.transcript = selectedCard.getTranscript();
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
