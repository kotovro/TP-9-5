package logic.video_processing.vosk.analiseDTO;

public class RawReplica {
    public String text;
    public RawSpeaker speaker;
    public double[] speakerVoiceSample;
    public double frameCount;
    private double startTime;


    public RawReplica(String text, RawSpeaker speaker, double[] speakerVoiceSample, double frameCount) {
        this.text = text;
        this.speaker = speaker;
        this.speakerVoiceSample = speakerVoiceSample;
        this.frameCount = frameCount;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
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