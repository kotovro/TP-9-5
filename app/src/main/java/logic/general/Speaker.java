package logic.general;

import javafx.scene.image.Image;

public class Speaker {
    private Long id;
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

    public Long getId() { return id;}
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name; // для отображения по умолчанию
    }

}
