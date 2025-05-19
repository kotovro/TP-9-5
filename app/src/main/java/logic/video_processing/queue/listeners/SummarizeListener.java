package logic.video_processing.queue.listeners;

import logic.general.Protocol;
import logic.general.Task;

import java.util.List;

public interface SummarizeListener {
    void onResultReady(Protocol protocol, List<Task> tasks);
}
