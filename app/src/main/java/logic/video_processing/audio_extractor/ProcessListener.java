package logic.video_processing.audio_extractor;

import logic.video_processing.queue.Processor;

public interface ProcessListener {
    void notifyStart(Processor processor);
    void notifyStop(Processor processor);
}
