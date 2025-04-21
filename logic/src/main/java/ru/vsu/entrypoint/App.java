package ru.vsu.entrypoint;

import ru.vsu.video_handler_sub_system.VideoToAudioExtractor;
import ru.vsu.video_upload_sub_system.VideoUploader;

import java.io.File;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        File videoFile = VideoUploader.loadVideo();
        VideoToAudioExtractor.extractAudio(videoFile, "C:\\Users\\1next\\Downloads\\extracted_audio.mp3");
    }
}
