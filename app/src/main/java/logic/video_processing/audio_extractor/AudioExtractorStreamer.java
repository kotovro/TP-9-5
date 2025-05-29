package logic.video_processing.audio_extractor;

import logic.video_processing.audioInput.AudioStreamConsumer;
import logic.video_processing.queue.Processor;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ShortBuffer;

public class AudioExtractorStreamer implements Processor {

    private final AudioFormat format;
    private long totalFrames = -1;
    private long processedFrames = 0;
    private ProcessListener processListener = new DeafProcessListener();

    public AudioExtractorStreamer() {
        this.format = new AudioFormat(16000, 16, 1, true, false);
    }

    /**
     * Стримит аудиодорожку из видеофайла в чанках в consumer, а после возвращает итоговый AudioInputStream.
     * @param filePath путь к видеофайлу
     * @param audioConsumer интерфейс обработки чанков
     * @return итоговый AudioInputStream после завершения
     */
    public void processAudio(String filePath, AudioStreamConsumer audioConsumer) {
        processedFrames = 0;
        totalFrames = 1;
        processListener.notifyStart(this);

        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(filePath)) {

            grabber.setSampleRate(16000);
            grabber.setAudioChannels(1);
            grabber.start();

            long duration = grabber.getLengthInTime() / 1000;
            totalFrames = duration * 16000 / 1000;

            Frame frame;
            final int CHUNK_SIZE_FRAMES = 1024;
            byte[] chunkBuffer = new byte[CHUNK_SIZE_FRAMES * 2];
            int bufferPos = 0;

            while ((frame = grabber.grabSamples()) != null) {
                if (frame.samples != null && frame.samples.length > 0) {
                    ShortBuffer sb = (ShortBuffer) frame.samples[0];
                    sb.rewind();
                    int remaining = sb.remaining();

                    for (int i = 0; i < remaining; i++) {
                        short val = sb.get();
                        chunkBuffer[bufferPos++] = (byte) (val & 0xFF);
                        chunkBuffer[bufferPos++] = (byte) ((val >> 8) & 0xFF);
                        processedFrames++;

                        if (bufferPos == chunkBuffer.length) {
                            audioConsumer.onAudioChunkReceived(chunkBuffer, bufferPos);
                            bufferPos = 0;
                        }
                    }
                }
            }

            if (bufferPos > 0) {
                audioConsumer.onAudioChunkReceived(chunkBuffer, bufferPos);
            }

            grabber.stop();
        } catch (Exception e) {
            System.err.println("Ошибка при извлечении аудио: " + e.getMessage());
            throw new RuntimeException("Не удалось извлечь аудио", e);
        } finally {
            totalFrames = -1;
            processedFrames = 0;
            processListener.notifyStop(this);
        }
    }

    public void subscribe(ProcessListener processListener) {
        this.processListener = processListener;
    }

    public int getProcessPercent() {
        if (totalFrames <= 0) {
            return -1;
        }
        return (int) ((processedFrames * 100) / totalFrames);
    }

}
