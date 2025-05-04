package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;

public class SavingController {
    @FXML
    private Button Save;

    @FXML
    private Pane errorPane;

    @FXML
    protected void onFileButtonClick(ActionEvent event) {
        errorPane.setVisible(false);
        // проверка, есть ли в базе такая стенограмма
        // ошибка (errorPane.setVisible(true);) если такая стенограмма существует;
    }
}
