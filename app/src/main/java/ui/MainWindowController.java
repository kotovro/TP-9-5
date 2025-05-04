package ui;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class MainWindowController {
    private final int MENU_WIDTH = 200;
    private boolean isMenuOpen = false;

    @FXML
    private VBox sideMenu;

    @FXML
    private Button menuButton;

    @FXML
    private void initialize() {
        menuButton.setOnAction(event -> toggleMenu());
    }

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
}