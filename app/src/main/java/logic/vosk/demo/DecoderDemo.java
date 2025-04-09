package logic.vosk.demo;

import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.vosk.LogLevel;
import org.vosk.Recognizer;
import org.vosk.LibVosk;
import org.vosk.Model;

public class DecoderDemo {

    public static void main(String[] argv) throws LineUnavailableException {
        LibVosk.setLogLevel(LogLevel.DEBUG);

        MicrophoneStreamer streamer = new MicrophoneStreamer();
        VoskAnalyzer analyzer = new VoskAnalyzer();

        streamer.startStreaming(analyzer);
    }
}
