package logic.audio_extractor;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AudioExtractor {

    public static AudioInputStream extractAudio(String fileAbsolutePath) {
        String[] command = {
                "ffmpeg",
                "-i", fileAbsolutePath,
                "-map", "0:a",
                "-acodec", "pcm_s16le",
                "-ac", "1",
                "-ar", "16000",
                "-f", "wav",
                "-"
        };
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();

            new Thread(() -> {
                try (BufferedReader err = new BufferedReader(
                        new InputStreamReader(process.getErrorStream()))) {
                    String line;
                    while ((line = err.readLine()) != null) {
                        System.err.println("FFmpeg: " + line);
                    }
                } catch (IOException ignored) {}
            }).start();

            BufferedInputStream audioStream = new BufferedInputStream(process.getInputStream());
            return AudioSystem.getAudioInputStream(audioStream);

        } catch (IOException | UnsupportedAudioFileException e) {
            throw new RuntimeException("Error during audio extraction: " + e.getMessage(), e);
        }
    }
}
