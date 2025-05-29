package logic.video_processing.queue.listeners;

import logic.general.Transcript;

import java.util.List;

public interface QueueChangeListener {
    void onQueueChange(List<String> taskQueue);
}
