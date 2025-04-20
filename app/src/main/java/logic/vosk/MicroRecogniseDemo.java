package logic.vosk;

import javax.sound.sampled.LineUnavailableException;

import logic.audioInput.MicrophoneStreamer;
import org.vosk.LogLevel;
import org.vosk.LibVosk;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MicroRecogniseDemo {

    public static void main(String[] argv) throws LineUnavailableException, InterruptedException, IOException {
//        LibVosk.setLogLevel(LogLevel.DEBUG);
        VoskAnalyzer analyzer = new VoskAnalyzer();
        VoskRecognizer recognizer = new VoskRecognizer(analyzer.getRecognizer());
        MicrophoneStreamer streamer = new MicrophoneStreamer();
        streamer.startStreaming(recognizer);

        System.out.println("MicroRecogniseDemo started");
        Thread.sleep(30000);

        streamer.stopStreaming();
        System.out.println("MicroRecogniseDemo finished");

        for (var replica : recognizer.getFinalResult()) {
            System.out.println(replica);
        }
    }
}
