package logic.persistence.exception;

public class SpeakerNotFoundException extends RuntimeException {
    public SpeakerNotFoundException(int speakerId) {
        super("Speaker with ID " + speakerId + " not found.");
    }
}
