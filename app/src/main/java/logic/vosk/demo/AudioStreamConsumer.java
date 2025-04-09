package logic.vosk.demo;

import javax.sound.sampled.AudioInputStream;

@FunctionalInterface
interface AudioStreamConsumer {
    void onAudioChunkReceived(AudioInputStream audioStream);
}
