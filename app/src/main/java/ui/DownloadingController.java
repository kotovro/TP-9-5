package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import logic.audio_extractor.AudioExtractorStreamer;
import logic.audio_extractor.VideoValidator;
import logic.general.Replica;
import logic.general.Speaker;
import logic.general.Transcript;
import logic.vosk.VoskRecognizer;
import logic.vosk.analiseDTO.RawReplica;

import java.io.File;
import java.util.Date;
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

    @FXML
    public void initialize() {
        errorPane.setVisible(false);
        sucsessPane.setVisible(false);

        initImages();
        initDropPaneEvents();
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
        VoskRecognizer recognizer = new VoskRecognizer();
        AudioExtractorStreamer streamer = new AudioExtractorStreamer();
        streamer.processAudio(selectedFile.getAbsolutePath(), recognizer::processStream);

        Transcript transcript = new Transcript("untitled", new Date());
        for (RawReplica replica : recognizer.getFinalResult()) {
            Speaker speaker = new Speaker("Speaker" + replica.speaker.ID, getDefaultImage(), replica.speaker.ID);
            transcript.addReplica(new Replica(replica.text, speaker));
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
        for (String format : VideoValidator.getSupportedFormats()) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(format, "*" + format));
        }
        return fileChooser;
    }

    private Image getDefaultImage() { //TODO
        return null;
    }
}
