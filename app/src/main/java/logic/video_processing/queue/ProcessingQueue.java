package logic.video_processing.queue;

import logic.video_processing.ProcessStatus;
import logic.video_processing.audio_extractor.AudioExtractorStreamer;
import logic.video_processing.audio_extractor.ProcessListener;
import logic.video_processing.vosk.VoskRecognizer;

import java.util.LinkedList;
import java.util.Queue;

public class ProcessingQueue {
    Queue<String> extractionTasks = new LinkedList<>();

    VoskRecognizer voskRecognizer = new VoskRecognizer();
    AudioExtractorStreamer audioExtractorStreamer = new AudioExtractorStreamer();

    ProcessStatus processStatus = ProcessStatus.WAITING_FOR_START;
    StatusListener statusListener = new DeafStatusListener();

    public void add(String path) {
        extractionTasks.add(path);
    }

    public void processTask() {
        processStatus = ProcessStatus.MODEL_UPLOAD;
        statusListener.onStatusChanged(processStatus);

        if (!voskRecognizer.isInit()) voskRecognizer.init();

        processStatus = ProcessStatus.TASK_PROCESSING;
        statusListener.onStatusChanged(processStatus);

        if (!extractionTasks.isEmpty()) {
            String extractionPath = extractionTasks.poll();
            audioExtractorStreamer.processAudio(extractionPath, voskRecognizer);
        }

        processStatus = ProcessStatus.MODEL_UNLOAD;
        statusListener.onStatusChanged(processStatus);
        voskRecognizer.freeResources();

        statusListener.onStatusChanged(ProcessStatus.TASK_FINISHED);
        processStatus = ProcessStatus.WAITING_FOR_START;
    }

    public ProcessStatus getProcessStatus() {
        return processStatus;
    }

    public void setStatusListener(StatusListener statusListener) {
        this.statusListener = statusListener;
    }

    public void setProcessListener(ProcessListener processListener) {
        audioExtractorStreamer.subscribe(processListener);
    }
}
