package logic.persistence.dao;

import logic.general.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MeetingMaterialsDao {
    private final Connection connection;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    private TranscriptDao transcriptDao;
    private TaskDao taskDao;
    private ProtocolDao protocolDao;

    public MeetingMaterialsDao(Connection connection, TranscriptDao transcriptDao, TaskDao taskDao, ProtocolDao protocolDao) {
        this.connection = connection;
        this.transcriptDao = transcriptDao;
        this.taskDao = taskDao;
        this.protocolDao = protocolDao;
    }

    public List<MeetingMaterials> getAllMeetingMaterials() {
        try {
            List<Transcript> transcripts = transcriptDao.getTranscripts();

            List<Protocol> protocols = protocolDao.getAllProtocols();
            Map<Integer, Protocol> protocolsByTranscript = new HashMap<>();
            for (Protocol protocol : protocols) {
                protocolsByTranscript.put(protocol.getTranscriptId(), protocol);
            }

            Map<Integer, List<Task>> tasksByTranscript = new HashMap<>();
            for (Transcript transcript : transcripts) {
                List<Task> tasks = taskDao.getTasksByTranscriptId(transcript.getId());
                tasksByTranscript.put(transcript.getId(), tasks);
            }

            List<MeetingMaterials> result = new ArrayList<>();
            for (Transcript transcript : transcripts) {
                int transcriptId = transcript.getId();

                Optional<Protocol> protocol = Optional.ofNullable(protocolsByTranscript.get(transcriptId));
                List<Task> tasks = tasksByTranscript.getOrDefault(transcriptId, new ArrayList<>());
                
                MeetingMaterials materials = new MeetingMaterials(transcript, protocol, tasks);
                result.add(materials);
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Error retrieving meeting materials", e);
        }
    }

}
