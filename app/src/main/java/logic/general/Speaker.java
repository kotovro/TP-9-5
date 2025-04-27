package logic.general;

import javafx.scene.image.Image;

public class Speaker {
    private int id;
    private final Image image;
    private final String name;



    public void setId(int id) {
        this.id = id;
    }

    public Speaker(String name, Image image, int id) {
        this.name = name;
        this.image =  image;
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public Image getImage() {
        return image;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }

}
