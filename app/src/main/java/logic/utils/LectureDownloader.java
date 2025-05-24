package logic.utils;

import logic.video_processing.queue.Processor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class LectureDownloader implements Processor {
    private long bytesDownloaded = 0;
    private long totalBytes = -1;

    public File downloadLectureVideo(String lectureURL) throws IOException {
        File outputFile = new File("dynamic-resources/lectures/lecture.webm");
        try {
            downloadFile(lectureURL, outputFile);
            return outputFile;
        } catch (IOException e) {
            String mp4Url = lectureURL.replace("webm", "mp4");
            outputFile = new File("dynamic-resources/lectures/lecture.mp4");
            downloadFile(mp4Url, outputFile);
            return outputFile;
        }
    }

    private void downloadFile(String urlString, File outputFile) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        totalBytes = connection.getContentLength();
        bytesDownloaded = 0;

        try (BufferedInputStream in = new BufferedInputStream(url.openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
            byte dataBuffer[] = new byte[8192];
            int bytesRead;

            while ((bytesRead = in.read(dataBuffer, 0, 8192)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
                bytesDownloaded += bytesRead;
            }
        }
    }

    @Override
    public int getProcessPercent() {
        return (int) (bytesDownloaded * 100 / totalBytes);
    }
}
