package logic.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

public class LectureDownloader {

    private static String constructVideoUrl(String baseUrl, String format) {
        String[] urlParts = baseUrl.split("/");
        return String.format("%s//%s/%s/%s/video/webcams.%s",
                urlParts[0],
                urlParts[2],
                urlParts[4],
                urlParts[6],
                format
        );
    }

    /**
     *
     * @param lectureURL
     * URL лекции с BigBlueButton(bbb)
     * @return
     * outputFile видеофайл  - класса File - в формате webm или mp4
     * @throws IOException
     *
     */
    public static File downloadLectureVideo(String lectureURL) throws IOException {
        String webmFormatUrl = constructVideoUrl(lectureURL, "webm");
        String mp4FormatUrl = constructVideoUrl(lectureURL, "mp4");

        try {
            File outputFile = new File("lecture.webm");
            downloadFile(webmFormatUrl, outputFile);
            return outputFile;
        } catch (IOException e) {
            File outputFile = new File("lecture.mp4");
            downloadFile(mp4FormatUrl, outputFile);
            return outputFile;
        }
    }

    private static void downloadFile(String urlString, File outputFile) throws IOException {
        try (BufferedInputStream in = new BufferedInputStream(URI.create(urlString).toURL().openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        }
    }
}
