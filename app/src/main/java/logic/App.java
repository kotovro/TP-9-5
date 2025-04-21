package logic;

import java.io.File;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        File videoFile = VideoUploader.loadVideo();
        VideoToAudioExtractor.extractAudio(videoFile, "C:\\raw_videos\\ЧМИ\\Очки.mp4");
    }
}
