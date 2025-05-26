package logic.video_processing.queue.listeners;

import logic.video_processing.ProcessStatus;

public interface StatusListener {
    void onStatusChanged(ProcessStatus status);
}
