package logic.vosk;

import javax.sound.sampled.LineUnavailableException;

import logic.audio_extractor.AudioExtractorStreamer;
import logic.audio_extractor.VideoValidator;

import java.io.IOException;

public class MicroRecogniseDemo {

    public static void main(String[] argv) throws LineUnavailableException, InterruptedException, IOException {
//        LibVosk.setLogLevel(LogLevel.DEBUG);
        String path = "C:\\Ребята, вы славно потрудились.mp4";
        if (!VideoValidator.isSupportVideoFile(path)) {
            throw new RuntimeException("Unsupported format");
        }
        VoskRecognizer recognizer = new VoskRecognizer();
        AudioExtractorStreamer streamer = new AudioExtractorStreamer();
        streamer.processAudio(path, recognizer::processStream);
        //streamer.startStreaming(recognizer);
//
//        System.out.println("MicroRecogniseDemo started");
//        Thread.sleep(30000);
//
//        streamer.stopStreaming();
//        System.out.println("MicroRecogniseDemo finished");

        for (var replica : recognizer.getFinalResult()) {
            System.out.println(replica);
        }
    }
}
