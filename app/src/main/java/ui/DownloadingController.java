package ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DownloadingController {
    @FXML
    private File selectedFile;

    @FXML
    private Button DownloadButton;

    @FXML
    private Pane errorPane;

    @FXML
    private Pane sucsessPane;

    @FXML
    private Pane dropPane;

    @FXML
    private ImageView ImageViewDrop;

    @FXML
    private ImageView imgCheckCircle;

    @FXML
    private ImageView imgDangerCircle;

    @FXML
    private ImageView imgUserSpeak2;

    private final List<String> allowed = List.of(".mp4", ".mov", ".avi", ".mkv", ".webm");

    private boolean isAllowedFile(File file) {
        String name = file.getName().toLowerCase();
        return allowed.stream().anyMatch(name::endsWith);
    }

    @FXML
    public void initialize() {
        errorPane.setVisible(false);
        sucsessPane.setVisible(false);

        initImages();
        initDropPaneEvents();
    }

    @FXML
    private Button loadButton;

    @FXML
    private void loadFromDatabase() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx_screens/loadStenogramm.fxml"));
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

    @FXML
    protected void onFileButtonClick(ActionEvent event) {
        errorPane.setVisible(false);
        sucsessPane.setVisible(false);
        FileChooser fileChooser = createFileChooser();
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            selectedFile = file;
            DownloadButton.setDisable(false);
            sucsessPane.setVisible(true);
        }
        else {
            errorPane.setVisible(true);
        }
    }

    @FXML
    protected void onDownloadButtonClick() {
        // то, что вам нужно сделать после загрузки файла
        try {
            DownloadButton.setDisable(true);
            Thread.sleep(5000);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx_screens/EditView.fxml"));
            Scene secondScene = new Scene(loader.load(), 600, 450);
            Stage stage = (Stage) DownloadButton.getScene().getWindow(); // Получаем текущую сцену
            stage.setScene(secondScene);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void initImages() {
        Image image = new Image(getClass().getResource("/images/CloudDownload.png").toExternalForm());
        ImageViewDrop.setImage(image);
        Image image2 = new Image(getClass().getResource("/images/CheckCircle.png").toExternalForm());
        imgCheckCircle.setImage(image2);
        Image image3 = new Image(getClass().getResource("/images/DangerCircle.png").toExternalForm());
        imgDangerCircle.setImage(image3);
        Image image4 = new Image(getClass().getResource("/images/UserSpeak2.png").toExternalForm());
        imgUserSpeak2.setImage(image4);
    }

    private void initDropPaneEvents() {
        dropPane.setOnDragOver(event -> {
            if (event.getGestureSource() != dropPane &&
                    event.getDragboard().hasFiles()) {
                boolean hasVideo = event.getDragboard().getFiles().stream()
                        .anyMatch(this::isAllowedFile);
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
                    if (isAllowedFile(file)) {
                        selectedFile = file;
                        DownloadButton.setDisable(false);
                        sucsessPane.setVisible(true);
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
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Видео файлы", "*.mp4"),
                new FileChooser.ExtensionFilter("Видео файлы", "*.mkv"),
                new FileChooser.ExtensionFilter("Видео файлы", "*.avi"),
                new FileChooser.ExtensionFilter("Видео файлы", "*.mov"),
                new FileChooser.ExtensionFilter("Видео файлы", "*.webm")
        );
        return fileChooser;
    }
}
