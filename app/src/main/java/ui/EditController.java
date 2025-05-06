package ui;

import javafx.animation.TranslateTransition;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.UnaryOperator;

import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import logic.general.Replica;
import logic.general.Transcript;
import logic.text_edit.EditStory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import logic.text_edit.EditStory;
import logic.text_edit.action.AddReplica;
import logic.text_edit.action.AddText;
import ui.hotkeys.HotkeysPressedActionProvider;

import java.util.List;

public class EditController {

    List<Pair<TextArea, Replica>> textAreas = new ArrayList<>();
    Replica currentReplica = null;

    private Transcript transcript = new Transcript("fjwiefjsw", new Date());
    private HotkeysPressedActionProvider hotkeysPressedActionProvider = new HotkeysPressedActionProvider();
    public static TextEditUnit getDifferenceWithIndex(String oldText, String newText) {
        int minLength = Math.min(oldText.length(), newText.length());
        int diffStart = 0;

        // Find start of difference
        while (diffStart < minLength && oldText.charAt(diffStart) == newText.charAt(diffStart)) {
            diffStart++;
        }

        // Find end of difference
        int diffEndOld = oldText.length() - 1;
        int diffEndNew = newText.length() - 1;
        while (diffEndOld >= diffStart && diffEndNew >= diffStart &&
                oldText.charAt(diffEndOld) == newText.charAt(diffEndNew)) {
            diffEndOld--;
            diffEndNew--;
        }

        String removed = oldText.substring(diffStart, diffEndOld + 1);
        String added = newText.substring(diffStart, diffEndNew + 1);



        return new TextEditUnit(diffStart, added, removed);
    }


//    public EditStory editStory = new EditStory();

    @FXML
    private AnchorPane rootPane = new AnchorPane();

    @FXML
    private VBox textAreaContainer;

    @FXML
    private Button saveButton;

    @FXML
    private Button saveButton1;

    @FXML
    private Button loadButton;
    private final int MENU_WIDTH = 200;
    private boolean isMenuOpen = false;

    @FXML
    private VBox sideMenu;

    @FXML
    private Button menuButton;

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
        // переход на главную
    }

    @FXML
    private void handleSavingsClick() {
        // переход на сохраненные файлы
    }

    @FXML
    private void handleEditClick() {
        // переход на обработать видео
    }

    @FXML
    private void handleExitClick() {
        System.exit(0);
    }

    @FXML
    public void initialize() {
        menuButton.setOnAction(event -> toggleMenu());
        List<Speaker> speakers = List.of(
                new Speaker("Anna", "/images/logo.png"),
                new Speaker("Boris", "/images/UserSpeak.png"),
                new Speaker("Viktor", "/images/UserSpeak2.png"),
                new Speaker("Galina", "/images/DangerCircle.png")
        );

        int replicas = 5;
        for (int i = 0; i < replicas; i++) {
            ComboBox<Speaker> comboBox = new ComboBox<>();
            comboBox.getItems().addAll(speakers);
            comboBox.setPrefWidth(160);
            comboBox.setPrefHeight(32);
            comboBox.getStyleClass().add("custom-combobox");

            comboBox.setCellFactory(lv -> new ListCell<>() {
                private final ImageView imageView = new ImageView();

                {
                    imageView.setFitHeight(20);
                    imageView.setFitWidth(20);
                    javafx.scene.shape.Circle clip = new javafx.scene.shape.Circle(10, 10, 10);
                    imageView.setClip(clip);
                }

                @Override
                protected void updateItem(Speaker item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        imageView.setImage(new Image(getClass().getResourceAsStream(item.getImagePath())));
                        setGraphic(imageView);
                        setText(item.getName());
                        setStyle("-fx-background-color: #9DA0FA; -fx-text-fill: white; -fx-border-color: #6366B5; -fx-border-width: 0 0 1px 0;");
                    }
                }
            });

            comboBox.setButtonCell(new ListCell<>() {
                private final ImageView imageView = new ImageView();

                {
                    imageView.setFitHeight(20);
                    imageView.setFitWidth(20);
                    javafx.scene.shape.Circle clip = new javafx.scene.shape.Circle(10, 10, 10);
                    imageView.setClip(clip);
                }

                @Override
                protected void updateItem(Speaker item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        imageView.setImage(new Image(getClass().getResourceAsStream(item.getImagePath())));
                        setGraphic(imageView);
                        setText(item.getName());
                        setStyle("-fx-text-fill: white;");
                    }
                }
            });

            VBox.setMargin(comboBox, new javafx.geometry.Insets(0, 0, 5, 0));
            textAreaContainer.getChildren().add(comboBox);

            Replica replica = new Replica("meimviece");

            //replica.setText();
            TextArea textArea = new TextArea();
            textArea.setWrapText(true);
            textArea.setPrefRowCount(1);
            textArea.setMinHeight(Region.USE_PREF_SIZE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            textArea.setPrefHeight(Region.USE_COMPUTED_SIZE);
            textArea.getStyleClass().add("custom-text-area");

            Text helper = new Text();
            helper.setFont(textArea.getFont());
            helper.setWrappingWidth(textArea.getWidth() - 20);
            helper.setText(textArea.getText());

            textArea.widthProperty().addListener((obs, oldWidth, newWidth) -> {
                helper.setWrappingWidth(newWidth.doubleValue() - 20);
                helper.setText(textArea.getText() + "\n ");
                textArea.setPrefHeight(helper.getLayoutBounds().getHeight() + 20);
            });

            textArea.textProperty().addListener((obs, oldText, newText) -> {
                helper.setText(newText + "\n ");
                double height = helper.getLayoutBounds().getHeight() + 20;
                textArea.setPrefHeight(height);
            });

            VBox.setMargin(textArea, new javafx.geometry.Insets(0, 0, 10, 0));
            textArea.setText("meimviece");
            textArea.setPrefRowCount(3);

            //TODO: extract lambdas into separate methods
            textArea.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    for (Pair<TextArea, Replica> pair : textAreas)
                    {
                        if (pair.getKey().focusedProperty().get())
                            currentReplica = pair.getValue();
                    }
                } else {
                    //System.out.println("TextArea lost focus");
                }
            });

            textArea.setTextFormatter(new TextFormatter<String>(change -> {

                if (change.isContentChange()) {
                    String allText = textArea.getText();
                    //replica.setText(allText);
                    String allTextAfterEdit = change.getControlNewText();

                    //TextEditUnit edits = getDifferenceWithIndex(allText, allTextAfterEdit);
                    //if (edits.deletedText().isEmpty() && !edits.insertedText().isEmpty())
                    //{
//                   //     editStory.addLast(new AddText(allText, replica, edits.textDifferenceStartIndex()));
                   // }

                    return change;
                } else {
                    // Ignore cursor movement-only changes
                    return change;
                }
            }));

            textAreas.add(new Pair<>(textArea, replica));
            textArea.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    for (Pair<TextArea, Replica> elements : textAreas) {
                        if (elements.getKey().focusedProperty().get())
                        {
                            System.out.println(elements.getKey().focusedProperty().get());
                        }
                    }
                } else {
                    System.out.println("TextArea lost focus");
                }
            });
            VBox.setMargin(textArea, new javafx.geometry.Insets(0, 0, 20, 0));
            textAreaContainer.getChildren().add(textArea);
        }

        saveButton.setOnAction(event -> {
            // то, что вам нужно (просто сохранить)
        });

        saveButton1.setOnAction(event -> {
            // то, что вам нужно (сохранить как)
        });

        loadButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx_screens/LoadOptional.fxml"));
                Scene scene = new Scene(loader.load());
                scene.getStylesheets().add(getClass().getResource("/styles/dialog-style.css").toExternalForm());

                Stage dialog = new Stage();
                dialog.setResizable(false);
                dialog.initOwner(loadButton.getScene().getWindow());
                dialog.setTitle("Выбор источника загрузки");
                dialog.setScene(scene);
                dialog.setResizable(false);

                LoadOptionDialogController controller = loader.getController();
                Stage mainStage = (Stage) loadButton.getScene().getWindow();
                controller.setMainStage(mainStage);

                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        textAreaContainer.getStyleClass().add("vbox-transparent");


    }
}
