package logic.vosk;

import javax.sound.sampled.AudioInputStream;
import java.io.IOException;

import logic.audioInput.AudioStreamConsumer;
import org.vosk.Recognizer;
import org.vosk.Model;

public class VoskAnalyzer {
    Recognizer recognizer;

    public Recognizer getRecognizer() {
        return recognizer;
    }

    public VoskAnalyzer() {
        try {
            Model model = new Model("resources/ai-models/speech-recognition-model");
            recognizer = new Recognizer(model, 16000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
