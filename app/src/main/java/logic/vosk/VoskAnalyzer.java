package logic.vosk;

import org.vosk.Model;
import org.vosk.Recognizer;

import java.io.IOException;

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
