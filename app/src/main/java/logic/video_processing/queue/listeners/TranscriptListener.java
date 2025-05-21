package logic.video_processing.queue.listeners;

import logic.general.Transcript;
import logic.video_processing.vosk.analiseDTO.RawTranscript;

public interface TranscriptListener {
    void onResultReady(RawTranscript rawTranscript);
}
