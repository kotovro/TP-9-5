package ru.vsu.video_handler_sub_system;

import java.io.File;
import java.io.IOException;

public class VideoToAudioExtractor {

    private static String findFFmpeg() {
        String[] paths = System.getenv("PATH").split(File.pathSeparator);
        for (String path : paths) {
            File ffmpeg = new File(path, "ffmpeg.exe");
            if (ffmpeg.exists() && ffmpeg.canExecute()) {
                return ffmpeg.getAbsolutePath();
            }
        }
        return null;
    }

    public static void extractAudio(File video, String audioPath) {
        String ffmpegPath = findFFmpeg();
        if (ffmpegPath != null) {
            String command = String.format("\"%s\" -i \"%s\" -vn -acodec libmp3lame \"%s\"",
                    ffmpegPath, video.getAbsolutePath(), audioPath);
            try {
                ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
                processBuilder.inheritIO();
                Process process = processBuilder.start();
                int exitCode = process.waitFor();
                System.out.println("Audio extracted from " + video.getAbsolutePath());
                if (exitCode == 0) {
                    System.out.println("Audio extracted by Path: " + audioPath);
                } else {
                    System.out.println("Error: " + exitCode);
                }
            } catch (IOException | InterruptedException e) {
                System.out.println("Error with audio extraction: " + e.getMessage());
            }
        } else {
            System.out.println("FFmpeg hasn't been found");
        }
    }
}
