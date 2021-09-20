package api.forks.util;

public class NumberUtil {

    public static int numbersOnly(String s) {
        String str = s.replaceAll("\\D+","");
        if (isInteger(str)) {
            return Integer.parseInt(str);
        } else {
            return -1;
        }
    }

    public static boolean isInteger(String s) {
        if (s == null) return false;
        return isInteger(s,10);
    }

    private static boolean isInteger(String s, int radix) {
        if (s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) return false;
                else continue;
            }
            if (Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }
}
