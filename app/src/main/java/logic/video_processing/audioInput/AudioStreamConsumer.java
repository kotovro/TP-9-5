package logic.video_processing.audioInput;

import javax.sound.sampled.AudioInputStream;

@FunctionalInterface
public interface AudioStreamConsumer {
    void onAudioChunkReceived(AudioInputStream audioStream);
}
