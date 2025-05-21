package ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class Tabs extends HBox {
    public Stenogramm stenogramm;
    public Label name = new Label();
    public ImageView close = new ImageView();

    public Tabs(Stenogramm stenogramm){
        this.stenogramm = stenogramm;
        this.setPrefHeight(49);
        this.name.setText(stenogramm.getName());
        this.close.setImage(new Image("/images/CloseCircle1.png"));
        this.close.setFitHeight(32);
        this.close.setFitWidth(32);
        this.getChildren().addAll(this.name, this.close);
    }

}
