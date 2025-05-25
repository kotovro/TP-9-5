package logic.utils;

public class TimeFormatter {
    private final double totalSeconds;

    public TimeFormatter(double seconds) {
        this.totalSeconds = seconds;
    }

    public String format() {
        int total = (int) totalSeconds;
        int hours = total / 3600;
        int remaining = total % 3600;
        int minutes = remaining / 60;
        int seconds = remaining % 60;

        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%d:%02d", minutes, seconds);
        }
    }
}
