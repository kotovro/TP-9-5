package logic.audio_extractor;

import logic.audioInput.AudioStreamConsumer;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ShortBuffer;

public class AudioExtractorStreamer {

    private volatile boolean isRunning = false;
    private final AudioFormat format;

    public AudioExtractorStreamer() {
        this.format = new AudioFormat(16000, 16, 1, true, false);
    }

    /**
     * Стримит аудиодорожку из видеофайла в чанках в consumer, а после возвращает итоговый AudioInputStream.
     * @param filePath путь к видеофайлу
     * @param audioConsumer интерфейс обработки чанков
     * @return итоговый AudioInputStream после завершения
     */
    public AudioInputStream streamAndReturnFullAudio(String filePath, AudioStreamConsumer audioConsumer) {
        isRunning = true;

        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(filePath)) {
            grabber.setSampleRate(16000);
            grabber.setAudioChannels(1);
            grabber.start();

            Frame frame;
            ByteArrayOutputStream fullOut = new ByteArrayOutputStream();

            while (isRunning && (frame = grabber.grabSamples()) != null) {
                if (frame.samples != null && frame.samples.length > 0) {
                    ShortBuffer sb = (ShortBuffer) frame.samples[0];
                    sb.rewind();
                    int remaining = sb.remaining();
                    byte[] buffer = new byte[remaining * 2];
                    for (int i = 0; i < remaining; i++) {
                        short val = sb.get();
                        buffer[i * 2] = (byte) (val & 0xFF);
                        buffer[i * 2 + 1] = (byte) ((val >> 8) & 0xFF);
                    }

                    // Сохраняем во всеобщий поток
                    fullOut.write(buffer);

                    // Отправляем чанком
                    ByteArrayInputStream chunkStream = new ByteArrayInputStream(buffer);
                    AudioInputStream audioChunk = new AudioInputStream(chunkStream, format, buffer.length / format.getFrameSize());
                    audioConsumer.onAudioChunkReceived(audioChunk);
                }
            }

            grabber.stop();

            // Возвращаем финальный stream
            byte[] audioBytes = fullOut.toByteArray();
            ByteArrayInputStream finalStream = new ByteArrayInputStream(audioBytes);
            return new AudioInputStream(finalStream, format, audioBytes.length / format.getFrameSize());

        } catch (Exception e) {
            System.err.println("Ошибка при извлечении аудио: " + e.getMessage());
            throw new RuntimeException("Не удалось извлечь аудио", e);
        }
    }

    public void stopStreaming() {
        isRunning = false;
    }
}
