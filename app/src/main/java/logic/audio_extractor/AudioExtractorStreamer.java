package logic.audio_extractor;

import logic.audioInput.AudioStreamConsumer;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ShortBuffer;

public class AudioExtractorStreamer {

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
    public void processAudio(String filePath, AudioStreamConsumer audioConsumer) {
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(filePath)) {
            grabber.setSampleRate(16000);
            grabber.setAudioChannels(1);
            grabber.start();

            Frame frame;
            ByteArrayOutputStream fullOut = new ByteArrayOutputStream();

            while ((frame = grabber.grabSamples()) != null) {
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
                    fullOut.write(buffer);
                }
            }

            grabber.stop();

            byte[] fullData = fullOut.toByteArray();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(fullData);
            AudioInputStream fullAudioStream = new AudioInputStream(inputStream, format, fullData.length / format.getFrameSize());

            audioConsumer.onAudioChunkReceived(fullAudioStream);

        } catch (Exception e) {
            System.err.println("Ошибка при извлечении аудио: " + e.getMessage());
            throw new RuntimeException("Не удалось извлечь аудио", e);
        }
    }

}
