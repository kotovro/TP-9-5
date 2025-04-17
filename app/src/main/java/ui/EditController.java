package ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import javafx.util.Pair;
import logic.general.Replica;
import logic.general.Transcript;
import logic.text_edit.EditStory;
import javafx.scene.layout.AnchorPane;
import logic.text_edit.action.AddReplica;
import logic.text_edit.action.AddText;
import ui.hotkeys.HotkeysPressedActionProvider;

public class EditController {


    List<Pair<TextArea, Replica>> textAreas = new ArrayList<>();
    Replica currentReplica = null;

    private Transcript transcript = new Transcript();
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
    public void initialize() {
        List<Speaker> speakers = List.of(
                new Speaker("Анна", "/images/logo.png"),
                new Speaker("Борис", "/images/UserSpeak.png"),
                new Speaker("Виктор", "/images/UserSpeak2.png"),
                new Speaker("Галина", "/images/DangerCircle.png")
        );
        int replicas = 5;
        for (int i = 0; i < replicas; i++) {
            ComboBox<Speaker> comboBox = new ComboBox<>();
            comboBox.getItems().addAll(speakers);

            comboBox.setCellFactory(lv -> new ListCell<>() {
                private final ImageView imageView = new ImageView();
                {
                    imageView.setFitHeight(24);
                    imageView.setFitWidth(24);
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
                    }
                }
            });

            comboBox.setButtonCell(new ListCell<>() {
                private final ImageView imageView = new ImageView();
                {
                    imageView.setFitHeight(24);
                    imageView.setFitWidth(24);
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
                    }
                }
            });


            VBox.setMargin(comboBox, new javafx.geometry.Insets(0, 0, 5, 0));

            textAreaContainer.getChildren().add(comboBox);
            Replica replica = new Replica();
            replica.setText("meimviece");
            TextArea textArea = new TextArea();
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
                    replica.setText(allText);
                    String allTextAfterEdit = change.getControlNewText();

                    TextEditUnit edits = getDifferenceWithIndex(allText, allTextAfterEdit);
                    if (edits.deletedText().isEmpty() && !edits.insertedText().isEmpty())
                    {
//                        editStory.addLast(new AddText(allText, replica, edits.textDifferenceStartIndex()));
                    }

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
            VBox.setMargin(textArea, new javafx.geometry.Insets(0, 0, 50, 0));
            textAreaContainer.getChildren().add(textArea);

            HotkeysPressedActionProvider actionProvider = new HotkeysPressedActionProvider();
            rootPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    actionProvider.attachHotkeys(newScene);
                }
            });
        }
    }
}