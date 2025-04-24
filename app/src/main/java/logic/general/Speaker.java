package logic.general;

import javafx.scene.image.Image;

public class Speaker {
    private final Image image;
    private final String name;

    public Speaker(String name, Image image) {
        this.name = name;
        this.image =  image;
    }

    public String getName() {
        return name;
    }

    public Image getImage() {
        return image;
    }

    @Override
    public String toString() {
        return name; // для отображения по умолчанию
    }

}
