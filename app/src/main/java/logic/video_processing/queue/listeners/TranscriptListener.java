package logic.video_processing.queue.listeners;

import logic.general.Transcript;

public interface TranscriptListener {
    void onResultReady(Transcript transcript);
}
