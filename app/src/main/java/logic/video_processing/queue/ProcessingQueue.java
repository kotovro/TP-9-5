package logic.video_processing.queue;

import logic.general.Protocol;
import logic.general.Task;
import logic.general.Transcript;
import logic.protocol.LLMWrapper;
import logic.video_processing.ProcessStatus;
import logic.video_processing.audio_extractor.AudioExtractorStreamer;
import logic.video_processing.audio_extractor.ProcessListener;
import logic.video_processing.queue.listeners.*;
import logic.video_processing.vosk.VoskRecognizer;
import logic.video_processing.vosk.analiseDTO.RawTranscript;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProcessingQueue implements Processor {
    private final BlockingQueue<String> makeTranscriptQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<Transcript> makeProtocolQueue = new LinkedBlockingQueue<>();

    private Thread processingThread;
    private final AtomicBoolean isProcessing = new AtomicBoolean(false);

    private final VoskRecognizer voskRecognizer = new VoskRecognizer();
    private final LLMWrapper llm = new LLMWrapper();
    private final AudioExtractorStreamer audioExtractorStreamer = new AudioExtractorStreamer();

    private ProcessStatus processStatus = ProcessStatus.WAITING_FOR_START;
    private StatusListener statusListener = new DeafStatusListener();
    private TranscriptListener transcriptListener = new DeafTranscriptListener();
    private SummarizeListener summarizeListener = new DeafSummarizeListener();

    private QueueState state = QueueState.NO_ACTIVE;

    public void add(String path) {
        makeTranscriptQueue.add(path);
        startProcessingIfNeeded();
    }

    public void add(Transcript transcript) {
        makeProtocolQueue.add(transcript);
        startProcessingIfNeeded();
    }

    public void setStatusListener(StatusListener statusListener) {
        this.statusListener = statusListener;
    }

    public void setProcessListener(ProcessListener processListener) {
        audioExtractorStreamer.subscribe(processListener);
    }

    public void setTranscriptListener(TranscriptListener transcriptListener) {
        this.transcriptListener = transcriptListener;
    }

    public int getProcessPercent() {
        return audioExtractorStreamer.getProcessPercent();
    }

    public void setSummarizeListener(SummarizeListener summarizeListener) {
        this.summarizeListener = summarizeListener;
    }

    private synchronized void startProcessingIfNeeded() {
        if (processingThread == null) {
            processingThread = new Thread(this::processAllTasks);
            processingThread.start();
        }
    }

    private void processAllTasks() {
        while (isTasksReady()) {
            processTask();
        }
        processingThread = null;
    }

    private void processTask() {
        initModelForTask();
        setProcessStatus(ProcessStatus.TASK_PROCESSING);

        if (state == QueueState.TRANSCRIPT_ACTIVE) {
            audioExtractorStreamer.processAudio(makeTranscriptQueue.poll(), voskRecognizer);
            RawTranscript rawTranscript = voskRecognizer.getFinalResult();
            transcriptListener.onResultReady(rawTranscript);
        }
        if (state == QueueState.PROTOCOL_ACTIVE) {
            Transcript transcript = makeProtocolQueue.poll();
            Protocol protocol = llm.summarize(transcript);
            List<Task> tasks = llm.getTasks(transcript);
            summarizeListener.onResultReady(transcript, protocol, tasks);
        }

        setProcessStatus(ProcessStatus.TASK_FINISHED);
        if (!isTasksReady()) closeModels();
    }

    private boolean isTasksReady() {
        return !(makeTranscriptQueue.isEmpty() && makeProtocolQueue.isEmpty());
    }

    private void initModelForTask() {
        if (state != QueueState.TRANSCRIPT_ACTIVE && makeProtocolQueue.isEmpty()) {
            setProcessStatus(ProcessStatus.MODEL_UNLOAD);
            llm.freeResources();
            setProcessStatus(ProcessStatus.MODEL_UPLOAD);
            voskRecognizer.init();
            state = QueueState.TRANSCRIPT_ACTIVE;
        }
        if (state != QueueState.PROTOCOL_ACTIVE && makeTranscriptQueue.isEmpty()) {
            setProcessStatus(ProcessStatus.MODEL_UNLOAD);
            voskRecognizer.freeResources();
            setProcessStatus(ProcessStatus.MODEL_UPLOAD);
            llm.init();
            state = QueueState.PROTOCOL_ACTIVE;
        }
    }

    private void closeModels() {
        if (llm.isInit()) llm.freeResources();
        if (voskRecognizer.isInit()) voskRecognizer.freeResources();
        state = QueueState.NO_ACTIVE;
    }

    public ProcessStatus getProcessStatus() {
        return processStatus;
    }

    private void setProcessStatus(ProcessStatus processStatus) {
        this.processStatus = processStatus;
        statusListener.onStatusChanged(processStatus);
    }
}
