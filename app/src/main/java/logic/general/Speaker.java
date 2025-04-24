package logic.general;

import javafx.scene.image.Image;

public class Speaker {
    private Long id;
    private final Image image;
    private final String name;

    public Speaker(String name, Image image, Long id) {
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

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }

}
