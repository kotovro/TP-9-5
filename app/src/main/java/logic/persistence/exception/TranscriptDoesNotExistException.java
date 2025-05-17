package logic.persistence.exception;

public class TranscriptDoesNotExistException extends RuntimeException {
    public TranscriptDoesNotExistException(String message) {
        super(message);
    }
}
