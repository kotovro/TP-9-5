package logic.persistence.exception;

public class EmptyTranscriptSaveAttemptException extends RuntimeException {
    public EmptyTranscriptSaveAttemptException(String message) {
        super(message);
    }
}
