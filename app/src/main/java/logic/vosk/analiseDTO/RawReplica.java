package logic.vosk.analiseDTO;

public class RawReplica {
    public String text;
    public RawSpeaker speaker;
    public double[] speakerVoiceSample;
    public double frameCount;


    public RawReplica(String text, RawSpeaker speaker, double[] speakerVoiceSample, double frameCount) {
        this.text = text;
        this.speaker = speaker;
        this.speakerVoiceSample = speakerVoiceSample;
        this.frameCount = frameCount;
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