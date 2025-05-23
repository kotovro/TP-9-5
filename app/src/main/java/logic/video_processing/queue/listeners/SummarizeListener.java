package logic.video_processing.queue.listeners;

import logic.general.Protocol;
import logic.general.Task;
import logic.general.Transcript;

import java.util.List;

public interface SummarizeListener {
    void onResultReady(Transcript transcript, Protocol protocol, List<Task> tasks);
}
