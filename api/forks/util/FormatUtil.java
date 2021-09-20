package api.forks.util;

public class FormatUtil {

    public static long getPerHour(long in, long time) {
        return (int) ((in) * 3600000D / time);
    }

    public static String formatTime(long time) {
        return String.format("%d:%02d:%02d", time / 3600, (time % 3600) / 60, (time % 60));
    }
}
