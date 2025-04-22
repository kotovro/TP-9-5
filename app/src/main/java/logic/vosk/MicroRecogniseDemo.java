package logic.vosk;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;

import logic.audioInput.AudioStreamConsumer;
import logic.audio_extractor.AudioExtractorStreamer;
import logic.audio_extractor.VideoValidator;

import java.io.IOException;

public class MicroRecogniseDemo {

    public static void main(String[] argv) throws LineUnavailableException, InterruptedException, IOException {
//        LibVosk.setLogLevel(LogLevel.DEBUG);
        String path = "C:\\Users\\1next\\Downloads\\Гайд для вернувшихся в хср.mp4";
        if (!VideoValidator.isSupportVideoFile(path)) {
            throw new RuntimeException("Unsupported format");
        }
        VoskAnalyzer analyzer = new VoskAnalyzer();
        VoskRecognizer recognizer = new VoskRecognizer(analyzer.getRecognizer());
        AudioExtractorStreamer streamer = new AudioExtractorStreamer();
        // Обрабатываем чанками
        AudioInputStream fullStream = streamer.streamAndReturnFullAudio(path, recognizer::processStream);
//        MicrophoneStreamer streamer = new MicrophoneStreamer();
//        streamer.startStreaming(recognizer);
//
//        System.out.println("MicroRecogniseDemo started");
//        Thread.sleep(30000);
//
//        streamer.stopStreaming();
//        System.out.println("MicroRecogniseDemo finished");


        recognizer.processStream(fullStream);

        for (var replica : recognizer.getFinalResult()) {
            System.out.println(replica);
        }

    }
}
