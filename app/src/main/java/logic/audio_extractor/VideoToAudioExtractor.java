package logic.audio_extractor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class VideoToAudioExtractor {

    public static InputStream extractAudio(File video) {
        String command = String.format("ffmpeg -i \"%s\" -f wav -acodec pcm_s16le -ar 44100 -ac 2 -",
                video.getAbsolutePath());
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            processBuilder.inheritIO();
            Process process = processBuilder.start();
            return process.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException("Error during audio extraction: " + e.getMessage(), e);
        }
    }
}
