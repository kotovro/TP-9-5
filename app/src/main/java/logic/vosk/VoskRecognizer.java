package logic.vosk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import logic.audioInput.AudioStreamConsumer;
import logic.vosk.analiseDTO.RawReplica;
import logic.vosk.analiseDTO.RawSpeaker;
import org.vosk.Model;
import org.vosk.Recognizer;
import org.vosk.SpeakerModel;

import javax.sound.sampled.AudioInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VoskRecognizer implements AudioStreamConsumer {
    private static final RawSpeaker UNDEFINED_SPEAKER = new RawSpeaker(-1, new double[]{});
    private static final double marginalDifference = 0.36;
    private static final double MINIMUM_FRAME_COUNT = 200;
    private static final int CHUNK_SIZE = 4096;
    private static final int MINIMUM_FREQUENCY = 3;

    private final Model model;
    private final Recognizer recognizer;
    private final List<RawSpeaker> speakers = new ArrayList<>();
    private final List<RawReplica> replicas = new ArrayList<>();
    private RawSpeaker currentSpeaker;


    public VoskRecognizer() {
        try {
            model = new Model("dynamic-resources/ai-models/speech-recognition-model");
            recognizer = new Recognizer(model, 16000);
            SpeakerModel model = new SpeakerModel("dynamic-resources/ai-models/speaker-recognition-model");
            recognizer.setSpeakerModel(model);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onAudioChunkReceived(AudioInputStream audioStream) {
        int nbytes;
        byte[] buffer = new byte[CHUNK_SIZE];
        try {
            while ((nbytes = audioStream.read(buffer)) >= 0) {
                if (recognizer.acceptWaveForm(buffer, nbytes)) {
                    var result = parseReplica(recognizer.getResult());
                    result.ifPresent(replicas::add);
                }
            }
        } catch (Exception e) {
            System.err.println("Can't read file");
        }
    }

    public void processStream(AudioInputStream audioStream) {
        onAudioChunkReceived(audioStream);
    }

    public List<RawReplica> getFinalResult() {
        try {
            var result = parseReplica(recognizer.getFinalResult());
            result.ifPresent(replicas::add);
        } catch (JsonProcessingException e) {
        }
        correctSpeakers();
        return replicas;
    }

    public void freeResources() {
        recognizer.close();
        model.close();
    }

    //Не оптимально
    private void correctSpeakers() {
        for (int i = 0; i < speakers.size(); i++) {
            RawSpeaker speaker1 = speakers.get(i);
            for (int j = i + 1; j < speakers.size(); j++) {
                RawSpeaker speaker2 = speakers.get(j);
                if (isClose(speaker1, speaker2)) {
                    speaker1.speakerVoiceSamples.addAll(speaker2.speakerVoiceSamples);
                    speaker1.count += speaker2.count;
                    speakers.remove(speaker2);
                    for (var replica : replicas) {
                        if (replica.speaker.equals(speaker2)) replica.speaker = speaker1;
                    }
                }
            }
        }

        for (int i = 0; i < speakers.size(); i++) {
            if (speakers.get(i).count < MINIMUM_FREQUENCY) {
                RawSpeaker speaker = speakers.get(i);
                speakers.remove(speaker);

                for (RawReplica replica : replicas) {
                    if (replica.speaker.equals(speaker)) {
                        replica.speaker = UNDEFINED_SPEAKER;
                    }
                }
                i--;
            }
        }

        for (int i = 0; i < speakers.size(); i++) {
            speakers.get(i).ID = i;
        }

    }

    private boolean isClose(RawSpeaker speaker1, RawSpeaker speaker2) {
        for (var sample1 : speaker1.speakerVoiceSamples) {
            for (var sample2 : speaker2.speakerVoiceSamples) {
                if (distance(sample1, sample2) < marginalDifference) {
                    return true;
                }
            }
        }
        return false;
    }

    private Optional<RawReplica> parseReplica(String jsonString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode rootNode = mapper.readTree(jsonString);

        JsonNode spkNode = rootNode.get("spk");
        if (spkNode == null) return Optional.empty();

        double[] spk = new double[spkNode.size()];
        for (int i = 0; i < spkNode.size(); i++) {
            spk[i] = spkNode.get(i).asDouble();
        }

        double spkFrames = rootNode.get("spk_frames").asDouble();

        String text = rootNode.get("text").asText();
        byte[] bytes = text.getBytes(Charset.forName("Windows-1251"));
        text = new String(bytes, StandardCharsets.UTF_8);
        text = text.substring(0, 1).toUpperCase() + text.substring(1);

        recognize(spk, spkFrames);
        return Optional.of(new RawReplica(text, currentSpeaker, spk, spkFrames));
    }

    private static double norm(double[] data) {
        double ans = 0.0;
        for (Double datum : data) {
            ans += datum * datum;
        }
        return (Math.sqrt(ans));
    }

    private static double dotProduct(double[] a, double[] b) throws ArithmeticException {
        if (a.length != b.length) {
            throw new ArithmeticException("The length of the vectors does not match!");
        }
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i] * b[i];
        }
        return sum;
    }

    private static double distance(double[] a, double[] b) {
        return 1 - dotProduct(a, b) / norm(a) / norm(b);
    }

    public void recognize(double[] sample, double frames) {
        if (frames < MINIMUM_FRAME_COUNT) {
            currentSpeaker = UNDEFINED_SPEAKER;
            return;
        }

        if (speakers.isEmpty()) {
            speakers.add(new RawSpeaker(0, sample));
            currentSpeaker = speakers.getLast();
        }

        for (RawSpeaker speaker : speakers) {
            double minDistance = Double.MAX_VALUE;
            for (double[] voiceSample : speaker.speakerVoiceSamples) {
                double distance = distance(voiceSample, sample);
                if (distance < minDistance) {
                    minDistance = distance;
                    speaker.distance = distance;
                }
            }
        }

        double minDistance = Double.MAX_VALUE;
        RawSpeaker closestSpeaker = speakers.getFirst();
        for (RawSpeaker user : speakers) {
            if (minDistance > user.distance) {
                minDistance = user.distance;
                closestSpeaker = user;
            }
        }

        if (closestSpeaker.distance < marginalDifference) {
            currentSpeaker = closestSpeaker;
            closestSpeaker.addVoiceSample(sample);
            return;
        }

        speakers.add(new RawSpeaker(speakers.size(), sample));
        currentSpeaker = speakers.getLast();
    }
}
