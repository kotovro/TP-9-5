package ui;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import logic.general.Transcript;

import java.util.Date;
import java.util.Objects;

public class MainWindowController implements PaneController {
    @FXML
    private ImageView animatedImageView;
    @FXML
    private Rectangle scrollRectangle;
    @FXML
    private Pane pane1;
    @FXML
    private Pane pane2;
    @FXML
    private Pane pane3;
    @FXML
    private Pane pane4;

    private Image image1;
    private Image image2;
    private boolean animationRunning = true;
    private Timeline timeline;
    private boolean toggled = false;
    double[] widths = {51, 101, 161, 236};
    private Pane[] panes;

    private int cardIndex = 0;

    @FXML
    private void scroll() {
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(scrollRectangle.widthProperty(), widths[cardIndex], Interpolator.EASE_BOTH);
        KeyFrame kf = new KeyFrame(Duration.millis(300), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();

        for (Pane pane : panes) {
            pane.setVisible(false);
        }
        panes[cardIndex].setVisible(true);

        cardIndex = (cardIndex + 1) % panes.length;
    }
    @FXML
    public void initialize() {
        panes = new Pane[]{pane1, pane2, pane3, pane4};
        image1 = new Image(Objects.requireNonNull(getClass().getResource("/images/menuV.png")).toExternalForm());
        image2 = new Image(Objects.requireNonNull(getClass().getResource("/images/menuV2.png")).toExternalForm());
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

    @Override
    public boolean load() {
        return true;
    }
}