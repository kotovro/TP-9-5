package logic.video_processing.queue;

import logic.general.MeetingMaterials;
import logic.general.Protocol;
import logic.general.Task;
import logic.general.Transcript;
import logic.persistence.DBManager;
import logic.protocol.LLMWrapper;
import logic.utils.LectureDownloader;
import logic.video_processing.ProcessStatus;
import logic.video_processing.audio_extractor.AudioExtractorStreamer;
import logic.video_processing.audio_extractor.ProcessListener;
import logic.video_processing.queue.listeners.*;
import logic.video_processing.vosk.VoskRecognizer;
import logic.video_processing.vosk.analiseDTO.RawTranscript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProcessingQueue implements Processor {
    private final BlockingQueue<String> makeTranscriptQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<Transcript> makeProtocolQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<String> downloadLectureQueue = new LinkedBlockingQueue<>();

    private Thread processingThread;
    private final AtomicBoolean isProcessing = new AtomicBoolean(false);

    private final VoskRecognizer voskRecognizer = new VoskRecognizer();
    private final LLMWrapper llm = new LLMWrapper();
    private final AudioExtractorStreamer audioExtractorStreamer = new AudioExtractorStreamer();
    private final LectureDownloader lectureDownloader = new LectureDownloader();

    private ProcessStatus processStatus = ProcessStatus.WAITING_FOR_START;
    private List<StatusListener> statusListeners = new ArrayList<>();
    private TranscriptListener transcriptListener = new DeafTranscriptListener();
    private SummarizeListener summarizeListener = new DeafSummarizeListener();
    private QueueChangeListener queueChangeListener = new DeafQueueChangeListener();

    private QueueState state = QueueState.NO_ACTIVE;

    public void addLecture(String URL) {
        downloadLectureQueue.add(URL);
        queueChangeListener.onQueueChange(getTaskPlan());
        startProcessingIfNeeded();
    }

    public void add(String path) {
        makeTranscriptQueue.add(path);
        queueChangeListener.onQueueChange(getTaskPlan());
        startProcessingIfNeeded();
    }

    public void add(Transcript transcript) {
        makeProtocolQueue.add(transcript);
        queueChangeListener.onQueueChange(getTaskPlan());
        startProcessingIfNeeded();
    }

    public void addStatusListener(StatusListener statusListener) {
        statusListeners.add(statusListener);
    }

    public void setProcessListener(ProcessListener processListener) {
        audioExtractorStreamer.subscribe(processListener);
        lectureDownloader.subscribe(processListener);
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

    public void setQueueChangeListener(QueueChangeListener queueChangeListener) {
        this.queueChangeListener = queueChangeListener;
    }

    private List<String> getTaskPlan() {
        List<String> taskPlan = new ArrayList<>();
        if (state == QueueState.TRANSCRIPT_ACTIVE || state == QueueState.LECTURE_ACTIVE) {
            for (String file : makeTranscriptQueue) {
                taskPlan.add(new File(file).getName());
            }
            for (String url : downloadLectureQueue) {
                taskPlan.add(url);
            }
            for (Transcript transcript : makeProtocolQueue) {
                taskPlan.add(transcript.getName());
            }
        } else if (state == QueueState.PROTOCOL_ACTIVE) {
            for (Transcript transcript : makeProtocolQueue) {
                taskPlan.add(transcript.getName());
            }
            for (String file : makeTranscriptQueue) {
                taskPlan.add(new File(file).getName());
            }
            for (String url : downloadLectureQueue) {
                taskPlan.add(url);
            }
        } else if (state == QueueState.NO_ACTIVE &&
                (!makeTranscriptQueue.isEmpty() || !downloadLectureQueue.isEmpty() || !makeProtocolQueue.isEmpty())) {
            for (String file : makeTranscriptQueue) {
                taskPlan.add(new File(file).getName());
            }
            for (String url : downloadLectureQueue) {
                taskPlan.add(url);
            }
            for (Transcript transcript : makeProtocolQueue) {
                taskPlan.add(transcript.getName());
            }
        }
        return taskPlan;
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
            String path = makeTranscriptQueue.poll();
            queueChangeListener.onQueueChange(getTaskPlan());
            audioExtractorStreamer.processAudio(path, voskRecognizer);
            RawTranscript rawTranscript = voskRecognizer.getFinalResult();
            transcriptListener.onResultReady(rawTranscript);
        }
        if (state == QueueState.LECTURE_ACTIVE) {
            String url = downloadLectureQueue.poll();
            queueChangeListener.onQueueChange(getTaskPlan());
            setProcessStatus(ProcessStatus.DOWNLOAD_LECTURE);
            try {
                File file = lectureDownloader.downloadLectureVideo(url);
                setProcessStatus(ProcessStatus.TASK_PROCESSING);
                audioExtractorStreamer.processAudio(file.getAbsolutePath(), voskRecognizer);
                RawTranscript rawTranscript = voskRecognizer.getFinalResult();
                transcriptListener.onResultReady(rawTranscript);
            } catch (Exception e) {
                setProcessStatus(ProcessStatus.TASK_FAILED);
                return;
            }
        }
        if (state == QueueState.PROTOCOL_ACTIVE) {
            Transcript transcript = makeProtocolQueue.poll();
            queueChangeListener.onQueueChange(getTaskPlan());
            Protocol protocol = llm.summarize(transcript);
            List<Task> tasks = llm.getTasks(transcript);
            summarizeListener.onResultReady(new MeetingMaterials(transcript, Optional.of(protocol), tasks));
        }

        setProcessStatus(ProcessStatus.TASK_FINISHED);
        if (!isTasksReady()) closeModels();
    }

    private boolean isTasksReady() {
        return !(makeTranscriptQueue.isEmpty() && makeProtocolQueue.isEmpty() && downloadLectureQueue.isEmpty());
    }

    private void initModelForTask() {
        if (state != QueueState.TRANSCRIPT_ACTIVE && state != QueueState.LECTURE_ACTIVE && makeProtocolQueue.isEmpty()) {
            setProcessStatus(ProcessStatus.MODEL_UNLOAD);
            llm.freeResources();
            setProcessStatus(ProcessStatus.MODEL_UPLOAD);
            voskRecognizer.init();
            if (!makeTranscriptQueue.isEmpty()) {
                state = QueueState.TRANSCRIPT_ACTIVE;
            } else {
                state = QueueState.LECTURE_ACTIVE;
            }
        } else if (state == QueueState.TRANSCRIPT_ACTIVE && makeTranscriptQueue.isEmpty() && !downloadLectureQueue.isEmpty()) {
            state = QueueState.LECTURE_ACTIVE;
        } else if (state != QueueState.PROTOCOL_ACTIVE && makeTranscriptQueue.isEmpty()) {
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
        for (StatusListener statusListener : statusListeners) {
            statusListener.onStatusChanged(processStatus);
        }
    }
}
