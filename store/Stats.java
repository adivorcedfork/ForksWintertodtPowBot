package store;

public class Stats {

    private static int crates = 0;

    public static int getCrates() {
        return crates;
    }

    public static void addCrates(int crates) {
        Stats.crates += crates;
    }
}
