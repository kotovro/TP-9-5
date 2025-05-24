package ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.image.ImageView;
import logic.general.Protocol;
import logic.general.Task;
import logic.general.Transcript;
import logic.video_processing.queue.ProcessingQueue;

import java.text.SimpleDateFormat;
import java.util.List;

public class StAndPrBuilder extends HBox {
    private Pane st = new Pane();
    private Button pr = new Button();

    public StAndPrBuilder(Transcript transcript, Protocol protocol, List<Task> tasks,
                          EditWindowController editWindowController, ProcessingQueue processingQueue) {
        Label name = new Label(transcript.getName());
        Font manropeFont = Font.loadFont(getClass().getResourceAsStream("/fonts/Manrope-ExtraBold.ttf"), 16);
        Font manropeFont2 = Font.loadFont(getClass().getResourceAsStream("/fonts/Manrope-Bold.ttf"), 16);
        Font manropeFont3 = Font.loadFont(getClass().getResourceAsStream("/fonts/Manrope-Regular.ttf"), 16);
        name.setStyle("-fx-font-family: \"Manrope ExtraBold\"; -fx-font-size: 18; -fx-text-fill: black;");
        name.setFont(manropeFont);
        name.setLayoutY(24);
        name.setLayoutX(18);

        Pane dateP = new Pane();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = sdf.format(transcript.getDate());
        Label date = new Label(formattedDate);
        date.setStyle("-fx-font-family: \"Manrope Regular\"; -fx-font-size: 14; -fx-text-fill: black;");
        date.setFont(manropeFont3);
        date.setLayoutY(10);
        date.setLayoutX(10);

        dateP.setLayoutY(72);
        dateP.setLayoutX(578);
        dateP.setPrefSize(92, 37);
        dateP.setStyle("-fx-background-color: #E7EFFF; -fx-background-radius: 8;");
        dateP.getChildren().add(date);


        ImageView im = new ImageView();
        im.setImage(new Image("/images/SquareAltArrowRight.png"));
        im.setFitWidth(37);
        im.setFitHeight(37);
        im.setLayoutX(633);
        im.setLayoutY(31);

        st.setPrefSize(684, 123);
        st.setLayoutY(27);
        st.setLayoutX(21);
        st.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 20; -fx-border-color: #2A55D5; -fx-border-width: 3; " +
                "-fx-border-radius: 20;");
        st.getChildren().addAll(name, dateP, im);

        pr.setFont(manropeFont2);
        if (protocol != null) {
            pr.setText("Открыть\nпротокол");
            pr.setOnAction(e -> {
                editWindowController.addProtocol(protocol, transcript, tasks);
            });
        } else {
            pr.setText("Создать\nпротокол");
            pr.setOnAction(e -> {
                processingQueue.add(transcript);
            });
        }
        pr.setStyle("-fx-background-color: #2A55D5; -fx-background-radius: 10; -fx-text-fill: white;");
        pr.setPrefSize(154, 50);
        setMargin(pr,  new Insets(30, 0, 5, 20));
        this.getChildren().addAll(st, pr);
    }
}
