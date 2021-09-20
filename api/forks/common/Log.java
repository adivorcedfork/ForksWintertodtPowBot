package api.forks.common;

public class Log {

    public static synchronized void info(String message) {
        System.out.println(message);
    }

    public static synchronized void severe(String message) {
        System.out.println(message);
    }

    public static synchronized void fine(String message) {
        System.out.println(message);
    }
}
