package ui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import logic.general.Transcript;

import java.util.Date;

public class MainWindowController implements PaneController {
    @FXML
    private ImageView animatedImageView;

    private Image image1;
    private Image image2;
    private boolean animationRunning = true;
    private Timeline timeline;

    @FXML
    public void initialize() {
        image1 = new Image(getClass().getResource("/images/menuV.png").toExternalForm());
        image2 = new Image(getClass().getResource("/images/menuV2.png").toExternalForm());
        animatedImageView.setImage(image1);

        startAnimation();
    }

    public void stopAnimation() {
        animationRunning = false;
        if (timeline != null) {
            timeline.stop();
            timeline = null;
        }
    }

    @Override
    public void startAnimation() {
        animationRunning = true;

        timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.2), event -> {
                    if (!animationRunning) return;
                    Image current = animatedImageView.getImage();
                    animatedImageView.setImage(current == image1 ? image2 : image1);
                })
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}