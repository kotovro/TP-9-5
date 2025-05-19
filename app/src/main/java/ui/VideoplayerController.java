package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;


public class VideoplayerController {

    @FXML
    private Label message;
    
    @FXML
    private Button pauseButton;

    @FXML
    private VBox root;

    private MediaPlayer mediaPlayer;
    private boolean isPaused = false;


    @FXML
    public void initialize() {
        pauseButton.setDisable(true);
    }

    @FXML
    protected void onChooseVideoButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Video File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi", "*.mov", "*.wmv", "*.mkv", "*.webm")
        );

        Stage stage = (Stage) root.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                // Stop and dispose previous media player if exists
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.dispose();
                }

                // Create new media player
                Media media = new Media(selectedFile.toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                // Set up media player event handlers
                mediaPlayer.setOnReady(() -> {
                    mediaView.setMediaPlayer(mediaPlayer);
                    pauseButton.setDisable(false);
                    pauseButton.setText("Pause");
                    isPaused = false;
                    mediaPlayer.play();
                });

                mediaPlayer.setOnError(() -> {
                    message.setText("Error playing video: " + mediaPlayer.getError().getMessage());
                    pauseButton.setDisable(true);
                });

            } catch (Exception e) {
                message.setText("Error loading video: " + e.getMessage());
                pauseButton.setDisable(true);
            }
        }
    }

    @FXML
    protected void onPauseButtonClick() {
        if (mediaPlayer != null && mediaPlayer.getStatus() != MediaPlayer.Status.UNKNOWN) {
            if (isPaused) {
                mediaPlayer.play();
                pauseButton.setText("Pause");
            } else {
                mediaPlayer.pause();
                pauseButton.setText("Play");
            }
            isPaused = !isPaused;
        }
    }
}
