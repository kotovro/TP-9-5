package ru.vsu.entrypoint;

import ru.vsu.VideoUploadSubSystem.VideoUploader;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        VideoUploader.loadVideo();
    }
}
