package logic.video_processing.audioInput;

import javax.sound.sampled.AudioInputStream;

public interface AudioStreamConsumer {
    void onAudioChunkReceived(AudioInputStream audioStream);

    void onAudioChunkReceived(byte[] audioData, int bytesRead);
}
