package logic.vosk.demo;

import javax.sound.sampled.AudioInputStream;
import java.io.IOException;

import org.vosk.Recognizer;
import org.vosk.Model;

public class VoskAnalyzer implements AudioStreamConsumer {
    Model model;
    Recognizer recognizer;

    public VoskAnalyzer() {
        try {
            model = new Model("resources/ai-models/speech-recognition-model");
            recognizer = new Recognizer(model, 16000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onAudioChunkReceived(AudioInputStream audioStream) {
        try {
            int nbytes;
            byte[] b = new byte[4096];
            while ((nbytes = audioStream.read(b)) >= 0) {
                if (recognizer.acceptWaveForm(b, nbytes)) {
                    System.out.println(recognizer.getResult());
                } else {
                    System.out.println(recognizer.getPartialResult());
                }
            }
        } catch (Exception e) {
            System.err.println("Can't read file");
        }
    }

    String getResult() {
        return recognizer.getFinalResult();
    }
}
