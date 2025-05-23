package logic.video_processing.vosk.analiseDTO;

import java.time.Instant;

public class RawReplica {
    public String text;
    public RawSpeaker speaker;
    public double[] speakerVoiceSample;
    public double frameCount;
    private Instant startInstant;


    public RawReplica(String text, RawSpeaker speaker, double[] speakerVoiceSample, double frameCount) {
        this.text = text;
        this.speaker = speaker;
        this.speakerVoiceSample = speakerVoiceSample;
        this.frameCount = frameCount;
    }

    public Instant getStartInstant() {
        return startInstant;
    }

    public void setStartInstant(Instant startInstant) {
        this.startInstant = startInstant;
    }

    @Override
    public String toString() {
        return "Replica {\n" +
               "text = '" + text + "'\n" +
               "speaker = " + speaker + "\n" +
               "frame count = " + frameCount + "\n" +
               '}';
    }
}