package ui;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.util.Duration;
import logic.video_processing.audio_extractor.VideoValidator;
import logic.video_processing.queue.ProcessingQueue;
import ui.custom_elements.ListenProgressBar;
import ui.custom_elements.Notification;

import java.io.File;

public class DownloadingController {
    @FXML
    private File selectedFile;

    @FXML
    private Button downloadButton;

    @FXML
    public Button loadFromFileButton;

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

    private final int MENU_WIDTH = 200;
    private boolean isMenuOpen = false;
    private final ProcessingQueue processingQueue = new ProcessingQueue();

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
    private ListenProgressBar progressBar;

    @FXML
    private Notification progressLabel;

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
        // через if else тут скорее всего нужно
        // if для редактирования ничего не выбрано:
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
        // else:
        //Stage stage = (Stage) change.getScene().getWindow();
        //Transcript transcript = new Transcript("untitled", new Date());
        //EditWindow.setStage(stage, transcript);
    }

    @FXML
    private void handleExitClick() {
        System.exit(0);
    }

    @FXML
    public void initialize() {
        menuButton.setOnAction(event -> toggleMenu());
        errorPane.setVisible(false);
        sucsessPane.setVisible(false);

        initImages();
        initDropPaneEvents();
        processingQueue.setProcessListener(progressBar);
        processingQueue.setStatusListener(progressLabel);
        progressBar.setProcessor(processingQueue);

        Platform.runLater(() -> {
            processingQueue.setResultListener(new ToEditSwitch((Stage) sucsessPane.getScene().getWindow()));
        });
    }

    @FXML
    private Button loadButton;

    @FXML
    private void loadFromDatabase() {
        loadButton.setDisable(true);
        downloadButton.setDisable(true);
        loadFromFileButton.setDisable(true);

        Stage stage = (Stage) loadButton.getScene().getWindow();
        LoadStenogrammApp.setStage(stage);
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
            downloadButton.setDisable(false);
            sucsessPane.setVisible(true);
        }
        else {
            errorPane.setVisible(true);
            downloadButton.setDisable(true);
        }
    }

    @FXML
    protected void onDownloadButtonClick() {
        downloadButton.setDisable(true);
        loadFromFileButton.setDisable(true);
        progressBar.setProgress(-1);

        Task<Void> recognitionTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                processingQueue.add(selectedFile.getAbsolutePath());
                processingQueue.processTask();
                return null;
            }
        };

        new Thread(recognitionTask).start();
    }

    private void initImages() {
        Image image = new Image(getClass().getResource("/images/CloudDownload.png").toExternalForm());
        ImageViewDrop.setImage(image);
        Image image2 = new Image(getClass().getResource("/images/CheckCircle.png").toExternalForm());
        imgCheckCircle.setImage(image2);
        Image image3 = new Image(getClass().getResource("/images/DangerCircle.png").toExternalForm());
        imgDangerCircle.setImage(image3);
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
}
