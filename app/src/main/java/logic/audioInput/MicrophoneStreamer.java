package logic.audioInput;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class MicrophoneStreamer {
    private TargetDataLine microphone;
    private AudioFormat format;
    private volatile boolean isRunning = false;
    private Thread streamingThread;

    public MicrophoneStreamer() throws LineUnavailableException {
        // Настройка формата аудио (16-bit, 44100 Hz, моно)
        this.format = new AudioFormat(16000, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)) {
            throw new LineUnavailableException("Микрофон не поддерживается!");
        }

        this.microphone = (TargetDataLine) AudioSystem.getLine(info);
        this.microphone.open(format);
    }

    /**
     * Запускает захват звука и передает данные в AudioInputStream.
     * @param audioConsumer Модуль, который будет обрабатывать аудиопоток.
     */
    public void startStreaming(AudioStreamConsumer audioConsumer) {
        if (isRunning) {
            System.err.println("Запись уже запущена!");
            return;
        }

        isRunning = true;
        microphone.start();

        byte[] buffer = new byte[4096];

        streamingThread = new Thread(() -> {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            while (isRunning) {
                int bytesRead = microphone.read(buffer, 0, buffer.length);
                if (bytesRead > 0) {
                    out.write(buffer, 0, bytesRead);

                    // Создаем AudioInputStream и передаем его в consumer
                    byte[] audioChunk = out.toByteArray();
                    ByteArrayInputStream bais = new ByteArrayInputStream(audioChunk);
                    AudioInputStream audioStream = new AudioInputStream(bais, format, audioChunk.length / format.getFrameSize());

                    audioConsumer.onAudioChunkReceived(audioStream);

                    out.reset(); // Очищаем буфер для следующего чанка
                }
            }

            microphone.stop();
            microphone.close();
        });
        streamingThread.start();
    }

    public void stopStreaming() {
        isRunning = false;
        while (streamingThread.isAlive()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}