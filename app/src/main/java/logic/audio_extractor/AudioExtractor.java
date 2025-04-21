package logic.audio_extractor;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;

public class AudioExtractor {

    public static AudioInputStream extractAudio(String fileAbsolutePath) {
        String[] command = String.format("ffmpeg -i \"%s\" -f mp4 -acodec pcm_s16le -ar 44100 -ac 2 -",
                fileAbsolutePath).split(" ");
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.inheritIO();
            Process process = processBuilder.start();
            return AudioSystem.getAudioInputStream(process.getInputStream());
        } catch (IOException | UnsupportedAudioFileException e) {
            throw new RuntimeException("Error during audio extraction: " + e.getMessage(), e);
        }
    }
}
