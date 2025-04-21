package logic.audio_extractor;
import java.io.File;
import java.util.Scanner;


public class VideoUploader {
    private static final String[] SUPPORTED_FORMATS = {".mp4", ".mkv", ".mov", ".avi", ".webm"};

    public static boolean isSupportedFormat(String fileName) {
        for (String format : SUPPORTED_FORMATS) {
            if (fileName.toLowerCase().endsWith(format)) {
                return true;
            }
        }
        return false;
    }

    public static File loadVideo() {
        Scanner scanner = new Scanner(System.in);
        File videoFile;
        while (true) {
            System.out.print("Please enter the path to your video file (MP4, MKV, MOV, AVI, WEBM): ");
            String filePath = scanner.nextLine();
            videoFile = new File(filePath);
            if (!videoFile.exists()) {
                System.out.println("The file does not exist. Please try again.");
                continue;
            }
            if (isSupportedFormat(filePath)) {
                System.out.println("Video loaded successfully: " + videoFile.getName());
                break;
            } else {
                System.out.println("Unsupported video format. Please upload a file with one of the following extensions: MP4, MKV, MOV, AVI, WEBM.");
            }
        }
        return videoFile;
    }

}
