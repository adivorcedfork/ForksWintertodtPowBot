package store;

import api.forks.common.Random;

public class Config {
    private static boolean setup = true;
    private static boolean stopping = false;

    public static int getLoopReturn() {
        return Random.nextInt(0, 50);
    }

    public static int getQuickLoopReturn() {
        return Random.nextInt(0, 10);
    }

    public static boolean isSetup() {
        return setup;
    }

    public static void setSetup(boolean setup) {
        Config.setup = setup;
    }

    public static boolean isStopping() {
        return stopping;
    }

    public static void setStopping(boolean stopping) {
        Config.stopping = stopping;
    }
}
