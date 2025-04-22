package logic.audio_extractor;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ShortBuffer;

public class AudioExtractor {

    public static AudioInputStream extractAudio(String filePath) {
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(filePath)) {
            grabber.start();

            if (grabber.getAudioChannels() == 0) {
                throw new RuntimeException("Файл не содержит аудио.");
            }
            int sampleRate = 16000;
            AudioFormat format = new AudioFormat(
                    sampleRate,
                    16,
                    grabber.getAudioChannels(),
                    true,
                    false
            );

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Frame frame;

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
                    out.write(buffer);
                }
            }

            grabber.stop();

            byte[] audioBytes = out.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
            return new AudioInputStream(bais, format, audioBytes.length / format.getFrameSize());

        } catch (Exception e) {
            System.err.println("Ошибка в процессе извлечения аудио: " + e.getMessage());
            throw new RuntimeException("Не удалось извлечь аудио: " + e.getMessage(), e);
        }
    }

}
