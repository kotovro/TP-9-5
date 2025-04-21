package logic.audio_extractor;

import logic.audio_extractor.exception.InvalidVideoFileException;

import java.nio.file.Files;
import java.nio.file.Path;


public class VideoValidator {
    private static final String[] SUPPORTED_FORMATS = {".mp4", ".mkv", ".mov", ".avi", ".webm"};

    public static boolean isSupportedFormat(String fileName) {
        for (String format : SUPPORTED_FORMATS) {
            if (fileName.endsWith(format)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isSupportVideoFile(String filePath) {
        return Files.exists(Path.of(filePath)) && isSupportedFormat(filePath);
    }
}
