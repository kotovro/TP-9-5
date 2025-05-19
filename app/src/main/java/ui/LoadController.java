package ui;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.video_processing.audio_extractor.VideoValidator;

import java.awt.*;
import java.io.File;

public class LoadController implements PaneController{
    @FXML
    private File selectedFile;
    @FXML
    private Pane dropPane;
    @FXML
    private Pane sucsessPane;
    @FXML
    private Label successLabel;
    @FXML
    private Pane errorPane;
    @FXML
    private Button downloadButton;

    @FXML
    public void initialize() {
        initDropPaneEvents();
    }

    @FXML
    protected void onDownloadButtonClick() {

    }

    @FXML
    protected void onFileButtonClick(ActionEvent event) {
        errorPane.setVisible(false);
        sucsessPane.setVisible(false);
        FileChooser fileChooser = createFileChooser();
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            selectedFile = file;
            successLabel.setText("Успешно! Файл " + file.getName() + " выбран");
            downloadButton.setDisable(false);
            sucsessPane.setVisible(true);
        }
        else {
            errorPane.setVisible(true);
            downloadButton.setDisable(true);
        }
    }


    private void initDropPaneEvents() {
        dropPane.setOnDragOver(event -> {
            if (event.getGestureSource() != dropPane &&
                    event.getDragboard().hasFiles()) {
                boolean hasVideo = event.getDragboard().getFiles().stream().map(File::getAbsolutePath)
                        .anyMatch(VideoValidator::isSupportVideoFile);
                if (hasVideo) {
                    event.acceptTransferModes(TransferMode.COPY);
                }
            }
            event.consume();
        });

        dropPane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                for (File file : db.getFiles()) {
                    if (VideoValidator.isSupportVideoFile(file.getAbsolutePath())) {
                        selectedFile = file;
                        downloadButton.setDisable(false);
                        sucsessPane.setVisible(true);
                        errorPane.setVisible(false);
                        success = true;
                        break;
                    }
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private FileChooser createFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файл");
        for (String format : VideoValidator.getSupportedFormats()) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(format, "*" + format));
        }
        return fileChooser;
    }

    @Override
    public void stopAnimation() {
    }

    @Override
    public void startAnimation() {
    }

    @Override
    public void load() {

    }
}
