package ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class DialogWindow {
    private final Stage dialogStage;
    private final OptionDialogController controller;

    public DialogWindow(Window owner) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx_screens/loadOptional.fxml"));
        Parent root = loader.load();
        controller = loader.getController();

        dialogStage = new Stage();
        //dialogStage.setTitle("Выбор источника загрузки");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(owner);
        dialogStage.setResizable(false);
        dialogStage.setScene(new Scene(root));
    }

    public void setDialogStage(Pane dialogP) {
        try {
            controller.dialogPane.getChildren().setAll(dialogP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show() {
        dialogStage.show();
    }

    public void close() {
        dialogStage.close();
    }
}
