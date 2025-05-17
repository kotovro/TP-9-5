package logic.video_processing.queue;

import logic.general.Transcript;

public interface ResultListener {
    void onResultReady(Transcript transcript);
}
