package logic.utils;

import logic.general.Protocol;
import logic.general.Replica;
import logic.general.Transcript;
import logic.persistence.dao.TranscriptDao;

import java.io.BufferedWriter;
import java.io.File;
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

    public static StringBuilder exportProtocolToTextFile(Transcript transcript, Protocol protocol, File file) {
        StringBuilder sb = new StringBuilder();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            sb.append("Protocol");
            sb.append(transcript.getName());
            sb.append("\n");
            sb.append(protocol.getText());
            writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb;
    }
}
