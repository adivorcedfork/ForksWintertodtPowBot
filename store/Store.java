package store;

import api.forks.common.Log;
import data.Activity;
import data.Food;
import data.Material;
import org.powbot.api.Area;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Store {

    // General
    private static Activity activity = Activity.WINTERTODT;
    private static String task = "Starting";

    // Config
    private static Food food;
    private static int foodAmount;
    private static int minFoodAmount;
    private static int eatAtPercent;
    private static int maxCrates;
    private static boolean shouldFletch;

    private static List<Material> neededMaterials = Arrays.asList(Material.AXE, Material.KNIFE, Material.TINDERBOX, Material.FOOD);

    // Stats
    private static int startingFiremakingXP = -1;
    private static int startingFiremakingLevel = -1;

    public static String getTask() {
        return task;
    }

    public static void setTask(String task) {
        Log.info("TASK: " + task);
        Store.task = task;
    }

    public static Activity getActivity() {
        return activity;
    }

    public static void setActivity(Activity activity) {
        Store.activity = activity;
    }

//    public static String getFoodName() {
//        return foodName;
//    }
//
//    public static void setFoodName(String foodName) {
//        Store.foodName = foodName;
//    }

    public static int getFoodAmount() {
        return foodAmount;
    }

    public static void setFoodAmount(int foodAmount) {
        Store.foodAmount = foodAmount;
    }

    public static int getMinFoodAmount() {
        return minFoodAmount;
    }

    public static void setMinFoodAmount(int minFoodAmount) {
        Store.minFoodAmount = minFoodAmount;
    }

    public static int getEatAtPercent() {
        return eatAtPercent;
    }

    public static void setEatAtPercent(int eatAtPercent) {
        Store.eatAtPercent = eatAtPercent;
    }

    public static int getMaxCrates() {
        return maxCrates;
    }

    public static void setMaxCrates(int maxCrates) {
        Store.maxCrates = maxCrates;
    }

    public static int getStartingFiremakingXP() {
        return startingFiremakingXP;
    }

    public static void setStartingFiremakingXP(int startingFiremakingXP) {
        Store.startingFiremakingXP = startingFiremakingXP;
    }

    public static int getStartingFiremakingLevel() {
        return startingFiremakingLevel;
    }

    public static void setStartingFiremakingLevel(int startingFiremakingLevel) {
        Store.startingFiremakingLevel = startingFiremakingLevel;
    }

    public static Food getFood() {
        return food;
    }

    public static void setFood(Food food) {
        Store.food = food;
    }

    public static List<Material> getNeededMaterials() {
        return neededMaterials;
    }

    public static void setNeededMaterials(List<Material> neededMaterials) {
        Store.neededMaterials = neededMaterials;
    }

    public static boolean shouldFletch() {
        return shouldFletch;
    }

    public static void setShouldFletch(boolean shouldFletch) {
        Store.shouldFletch = shouldFletch;
    }
}
