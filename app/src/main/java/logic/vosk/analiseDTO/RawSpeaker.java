package logic.vosk.analiseDTO;

import java.util.ArrayList;
import java.util.List;

public class RawSpeaker {
    public int ID;
    public List<double[]> speakerVoiceSamples = new ArrayList<double[]>();
    public double distance = Double.MAX_VALUE;
    public int count = 1;

    public RawSpeaker(int ID, double[] sample){
        this.ID = ID;
        this.speakerVoiceSamples.add(sample);
    }

    public void addVoiceSample(double[] sample) {
        speakerVoiceSamples.add(sample);
        count++;
    }

    @Override
    public String toString() {
        return "Speaker" + ID;
    }

    public boolean equals(RawSpeaker speaker) {
        return this.ID == speaker.ID;
    }
}