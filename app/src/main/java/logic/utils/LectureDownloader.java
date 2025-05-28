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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LectureDownloader implements Processor {
    private long bytesDownloaded = 0;
    private long totalBytes = -1;
    private ProcessListener processListener = new DeafProcessListener();

    private static final Pattern VIDEO_URL_PATTERN = Pattern.compile("^https?://bbb\\.edu\\.vsu\\.ru/presentation/.+/video/.*\\.(webm|mp4)$");

    public File downloadLectureVideo(String lectureURL) throws IOException {
        if (!isValidVideoUrl(lectureURL)) {
            throw new IllegalArgumentException("Invalid lecture video URL format: " + lectureURL);
        }
        if (!isDownloadable(lectureURL)) {
            throw new IOException("Lecture video resource is not downloadable from URL: " + lectureURL);
        }
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
        @SuppressWarnings("deprecation")
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

    private boolean isDownloadable(String lectureURL) {
        if (lectureURL == null || lectureURL.trim().isEmpty()) {
            return false;
        }
        try {
            @SuppressWarnings("deprecation")
            URL url = new URL(lectureURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return responseCode >= 200 && responseCode < 300;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isValidVideoUrl(String lectureURL) {
        if (lectureURL == null) {
            return false;
        }
        Matcher matcher = VIDEO_URL_PATTERN.matcher(lectureURL);
        return matcher.matches();
    }

    @Override
    public int getProcessPercent() {
        return (int) (bytesDownloaded * 100 / totalBytes);
    }

    public void subscribe(ProcessListener processListener) {
        this.processListener = processListener;
    }
}
