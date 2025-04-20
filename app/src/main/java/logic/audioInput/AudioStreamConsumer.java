package logic.audioInput;

import javax.sound.sampled.AudioInputStream;

@FunctionalInterface
public interface AudioStreamConsumer {
    void onAudioChunkReceived(AudioInputStream audioStream);
}
