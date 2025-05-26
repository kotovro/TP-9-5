package logic.utils;

public class TimeFormatter {
    public static TimeCode format(double totalSeconds) {
        int total = (int) totalSeconds;
        int hours = total / 3600;
        int remaining = total % 3600;
        int minutes = remaining / 60;
        int seconds = remaining % 60;

        return new TimeCode(hours, minutes, seconds);
    }

    public static double toSeconds(TimeCode timeCode) {
        return timeCode.getHour() * 3600 + timeCode.getMinute() * 60 + timeCode.getSecond();
    }
}
