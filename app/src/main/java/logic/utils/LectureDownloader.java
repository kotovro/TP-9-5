package logic.utils;

import logic.video_processing.audio_extractor.DeafProcessListener;
import logic.video_processing.audio_extractor.ProcessListener;
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
    private ProcessListener processListener = new DeafProcessListener();

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
        processListener.notifyStart(this);

        try (BufferedInputStream in = new BufferedInputStream(url.openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
            byte dataBuffer[] = new byte[8192];
            int bytesRead;

            while ((bytesRead = in.read(dataBuffer, 0, 8192)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
                bytesDownloaded += bytesRead;
            }
        } finally {
            processListener.notifyStop(this);
        }
    }

    @Override
    public int getProcessPercent() {
        return (int) (bytesDownloaded * 100 / totalBytes);
    }

    public void subscribe(ProcessListener processListener) {
        this.processListener = processListener;
    }
}
