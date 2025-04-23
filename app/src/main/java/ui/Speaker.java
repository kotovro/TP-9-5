package ui;

public class Speaker {
    private final String name;
    private final String imagePath; // путь к аватарке

    public Speaker(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    @Override
    public String toString() {
        return name; // для отображения по умолчанию
    }
}