package logic.utils;

import logic.general.Replica;
import logic.general.Transcript;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class EntitiesExporter {
    public static StringBuilder exportTranscriptToTextFile(Transcript transcript, String filePath) {
        StringBuilder sb = new StringBuilder();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            sb.append(transcript.getName()).append(" ");
            sb.append(transcript.getDate().toString());
            sb.append("\n");
            for (Replica replica : transcript.getReplicas()) {
                sb.append(replica.getSpeaker()).append(":\n");
                sb.append(replica.getText()).append("\n");
            }
            writer.write(sb.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb;
    }
}
