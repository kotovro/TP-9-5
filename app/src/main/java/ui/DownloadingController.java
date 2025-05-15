package ui;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
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
import logic.audio_extractor.AudioExtractorStreamer;
import logic.audio_extractor.VideoValidator;
import logic.general.Replica;
import logic.general.Speaker;
import logic.general.Transcript;
import logic.persistence.DBManager;
import logic.vosk.VoskRecognizer;
import logic.vosk.analiseDTO.RawReplica;

import java.io.File;
import java.util.Date;

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
    private ProgressBar progressBar;

    @FXML
    private Label progressLabel;

    @FXML
    private Label label; //Виталь, лабел для твоих состояний

    private void startLoadingTask() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (int i = 0; i <= 100; i++) {
                    Thread.sleep(50); // Имитация загрузки
                    updateProgress(i, 100); // Обновляем прогресс (0-1)
                }
                return null;
            }
        };

        progressBar.progressProperty().bind(task.progressProperty());
        progressLabel.textProperty().bind(
                task.progressProperty().multiply(100).asString("%.0f%%")
        );

        new Thread(task).start();
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
        startLoadingTask();
        menuButton.setOnAction(event -> toggleMenu());
        errorPane.setVisible(false);
        sucsessPane.setVisible(false);

        initImages();
        initDropPaneEvents();
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
        //loadF.setDisable(true);
        startLoadingTask(); // начало загрузки
        downloadButton.setDisable(true);
        loadFromFileButton.setDisable(true);

        Task<Void> recognitionTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                VoskRecognizer recognizer = new VoskRecognizer();
                AudioExtractorStreamer streamer = new AudioExtractorStreamer();
                streamer.processAudio(selectedFile.getAbsolutePath(), recognizer::processStream);

                //Speaker должке копироваться а не каждый раз браться
                Transcript transcript = new Transcript("untitled", new Date());
                for (RawReplica replica : recognizer.getFinalResult()) {
                    Speaker speaker = DBManager.getSpeakerDao().getSpeakerById(1);
                    transcript.addReplica(new Replica(replica.text, speaker));
                }

                Platform.runLater(() -> {
                    try {
                        Stage stage = (Stage) sucsessPane.getScene().getWindow();
                        EditWindow.setStage(stage, transcript);
                        recognizer.freeResources();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                return null;
            }
        };

        recognitionTask.setOnFailed(event -> {
            Throwable e = recognitionTask.getException();
            e.printStackTrace();
        });

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
