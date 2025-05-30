package logic.integration_test.util_test;

import logic.general.Protocol;
import logic.general.Replica;
import logic.general.Speaker;
import logic.general.Transcript;
import logic.utils.EntitiesExporter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class EntitiesExporterTest {
    private Transcript transcript;
    private Protocol protocol;

    @BeforeEach
    void setUp(){
        transcript = new Transcript("Test Transcript", new Date());
        Speaker speaker = new Speaker("Test Speaker", null, 0);
        transcript.setReplicas(Arrays.asList(
                new Replica("Content 1", speaker, 1),
                new Replica("Content 2", speaker, 2)
        ));
        protocol = new Protocol(1, "Test Conclusion");
    }

    @Test
    void testExportProtocolToTextFile_Success(@TempDir File tempDir) throws Exception {
        File tempFile = new File(tempDir, "protocol.txt");
        String expectedContent = String.format("Protocol for transcript Test Transcript %s\nTest Conclusion",
                transcript.getDate());
        StringBuilder result = EntitiesExporter.exportProtocolToTextFile(transcript, protocol, tempFile);

        assertEquals(expectedContent, result.toString(), "StringBuilder content should match");
        String fileContent = Files.readString(tempFile.toPath());
        assertEquals(expectedContent, fileContent, "File content should match");
    }

    @Test
    void testExportProtocolToTextFile_EmptyProtocolText(@TempDir File tempDir) throws Exception {
        Protocol emptyProtocol = new Protocol(1, "");
        File tempFile = new File(tempDir, "empty_protocol.txt");
        String expectedContent = String.format("Protocol for transcript Test Transcript %s\n", transcript.getDate());
        StringBuilder result = EntitiesExporter.exportProtocolToTextFile(transcript, emptyProtocol, tempFile);

        assertEquals(expectedContent, result.toString(), "StringBuilder content should match for empty protocol");
        String fileContent = Files.readString(tempFile.toPath());
        assertEquals(expectedContent, fileContent, "File content should match for empty protocol");
    }
}
