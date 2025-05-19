package logic.video_processing;

public enum ProcessStatus {
    MODEL_UPLOAD("Модель загружается"),
    TASK_PROCESSING("Видео обрабатывается"),
    TASK_FINISHED("Задача завершена"),
    MODEL_UNLOAD("Модель выгружается"),
    WAITING_FOR_START("Ожидание начала");

    private final String message;

    ProcessStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
