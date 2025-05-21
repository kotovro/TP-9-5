package ui;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class StAndPrBuilder extends HBox {
    public Pane st = new Pane();
    public Pane pr = new Pane();
    public StAndPrBuilder() {
        Label name = new Label("Название стенограммы"); //внутрь вставить название стенограммы
        name.setStyle("-fx-font-family: 'Manrope-ExtraBold'; -fx-font-size: 16; -fx-text-fill: black;");
        name.setLayoutY(14);
        name.setLayoutX(14);
        Pane date = new Pane();
        date.setLayoutY(79);
        date.setLayoutX(578);
        date.setPrefSize(92, 37);
        date.setStyle("-fx-background-color: #E7EFFF; -fx-background-radius: 8;");
        st.setPrefSize(684, 123);
        st.setLayoutY(27);
        st.setLayoutX(21);
        st.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-border-color: #2A55D5; -fx-border-width: 3; " +
                "-fx-border-radius: 20;");
        st.getChildren().addAll(name, date);
        this.getChildren().add(st);
    }
}
