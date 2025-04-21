package logic.audio_extractor;
import logic.audio_extractor.exception.InvalidVideoFileException;

import java.io.File;
import java.util.Scanner;


public class VideoUploader {
    private static final String[] SUPPORTED_FORMATS = {".mp4", ".mkv", ".mov", ".avi", ".webm"};

    public static boolean isSupportedFormat(String fileName) {
        for (String format : SUPPORTED_FORMATS) {
            if (fileName.endsWith(format)) {
                return true;
            }
        }
        return false;
    }

    private static void validateVideoFile(File file) throws InvalidVideoFileException {
        if (!file.exists() || !file.isFile()) {
            throw new InvalidVideoFileException("The file does not exist or is not a valid file.");
        }
        if (!isSupportedFormat(file.getName())) {
            throw new InvalidVideoFileException("Unsupported video format. Supported formats: MP4, MKV, MOV, AVI, WEBM.");
        }
    }

    public static File loadVideo() {
        Scanner scanner = new Scanner(System.in);
        File videoFile;
        System.out.print("Enter the path to your video file (MP4, MKV, MOV, AVI, WEBM): ");
        String filePath = scanner.nextLine().trim().replace("\"", "");
        videoFile = new File(filePath);
        try {
            validateVideoFile(videoFile);
            System.out.println("Video loaded successfully: " + videoFile.getName());
        } catch (InvalidVideoFileException e) {
            System.out.println(e.getMessage() + "\n");
        }
        return videoFile;
    }
}
