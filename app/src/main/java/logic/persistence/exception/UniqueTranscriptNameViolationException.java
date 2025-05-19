package logic.persistence.exception;

public class UniqueTranscriptNameViolationException extends RuntimeException {
    public UniqueTranscriptNameViolationException(String transcriptName) {
        super("Transcript with name " +  transcriptName + " already exists");
    }
}
