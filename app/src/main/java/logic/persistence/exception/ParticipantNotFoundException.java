package logic.persistence.exception;

public class ParticipantNotFoundException extends RuntimeException {
    public ParticipantNotFoundException(Long participantId) {
        super("Participant with ID " + participantId + " not found.");
    }
}
