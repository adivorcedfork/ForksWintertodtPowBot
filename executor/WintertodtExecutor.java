package executor;

import api.forks.common.Log;
import api.forks.common.Time;
import api.forks.game.ExInventory;
import api.forks.game.ExMovement;
import api.forks.game.ExObjects;
import api.forks.game.utility.Distance;
import data.Activity;
import data.Location;
import data.Material;
import data.Preds;
import helper.MovementHelper;
import helper.PlayerHelper;
import helper.WintertodtHelper;
import org.powbot.api.rt4.*;
import store.Config;
import store.Store;

public class WintertodtExecutor extends ActivityExecutor {

    private final int SLEEP_THRESH_MS = 2000;
    private final int MIN_BRAZIER_DIST = 2;
    private WintertodtActivity localActivity = WintertodtActivity.STARTING;

    @Override
    public int execute() {
        Log.info("Energy: " + WintertodtHelper.getEnergy()
                + " | Seconds: " + WintertodtHelper.getSecondsUntilReturn()
                + " | Current points: " + WintertodtHelper.getCurrentPoints());

        if (localActivity != WintertodtActivity.STARTING && WintertodtHelper.getSecondsUntilReturn() > 0) {
            localActivity = WintertodtActivity.STARTING;
        }

        if (!Location.WINTERTODT_AREA.contains(Players.local())) {
            Log.info("Not in Wintertodt area. Banking first...");
            Store.setActivity(Activity.BANKING);
            return Config.getQuickLoopReturn();
        }

        if (ExObjects.getNearest(Preds.DIRECT_SNOW_FALL) != null) {
            Store.setTask("Avoiding snow fall");
            MovementHelper.avoidSnowfall();
            return Config.getLoopReturn();
        }

        if (Players.local().healthPercent() <= Store.getEatAtPercent()) {
            Item food = ExInventory.getFirst(Material.FOOD.getInventoryPred());
            if (food != null) {
                Store.setTask("Eating");
                int previousHealth = Players.local().healthPercent();
                food.interact(a -> true);
                Time.sleepUntil(() -> previousHealth != Players.local().healthPercent(), 2000);
                return Config.getLoopReturn();
            }
            Log.severe("No food found");
        }

        if (!PlayerHelper.hasRequiredItems()) {
            Log.fine("Need required items! Banking to get them...");
            Store.setActivity(Activity.BANKING);
            localActivity = WintertodtActivity.STARTING;
            return Config.getLoopReturn();
        }

        GameObject brazier = ExObjects.getNearest(Preds.SW_BRAZIER);
        if (brazier == null) {
            Store.setTask("Walking to brazier");
            ExMovement.stepOrRandom(Location.WEST_BRAZIER_LOCATION);
            return Config.getLoopReturn();
        }

        if (Distance.to(Location.WEST_BRAZIER_LOCATION) <= MIN_BRAZIER_DIST && WintertodtHelper.getSecondsUntilReturn() <= 4) {
            if (brazier.actions().contains("Light")) {
                Store.setTask("Lighting brazier");
                if (!brazier.inViewport()) {
                    Camera.turnTo(brazier);
                }
                if (!brazier.interact(a -> true)) {
                    Log.severe("Failed to interact with the brazier");
                    return Config.getLoopReturn();
                }
                return Config.getLoopReturn();
            }

            if (brazier.actions().contains("Fix")) {
                Store.setTask("Fixing brazier");
                if (!brazier.inViewport()) {
                    Camera.turnTo(brazier);
                }
                if (!brazier.interact(a -> true)) {
                    Log.severe("Failed to interact and fix the brazier");
                    return Config.getLoopReturn();
                }
                return Config.getLoopReturn();
            }
        }

        switch (localActivity) {
            case STARTING:
                if (WintertodtHelper.getSecondsUntilReturn() == 0) {
                    Log.fine("Game has started. Let's chop some stuff.");
                    localActivity = WintertodtActivity.CHOPPING;
                    return Config.getLoopReturn();
                }

                if (ExInventory.getCount(Preds.SUPPLY_CRATE) > Store.getMaxCrates()) {
                    Log.fine("We need to deposit crates. Let's bank.");
                    Store.setActivity(Activity.BANKING);
                    return Config.getLoopReturn();
                }

                Item junkItem = ExInventory.getFirst(Preds.JUNK_ITEM);
                if (junkItem != null && WintertodtHelper.getSecondsUntilReturn() > 0) {
                    Store.setTask("Dropping junk item");
                    junkItem.interact("Drop");
                    return Config.getLoopReturn();
                }

                if (Distance.to(Location.WEST_BRAZIER_LOCATION) > MIN_BRAZIER_DIST) {
                    Store.setTask("Getting closer to brazier");
                    ExMovement.stepOrRandom(Location.WEST_BRAZIER_LOCATION);
                    return Config.getLoopReturn();
                }

                Store.setTask("Waiting for game to start");
                return Config.getLoopReturn();

            case CHOPPING:
                if (WintertodtHelper.isFinalFeedCall()) {
                    Log.fine("We need enough time to feed the brazier! Let's go now.");
                    localActivity = WintertodtActivity.FEEDING;
                    return Config.getLoopReturn();
                }

                if (ExInventory.isFull()) {
                    if (Store.shouldFletch()) {
                        Log.fine("We have a full inventory. Let's fletch!");
                        localActivity = WintertodtActivity.FLETCHING;
                        return Config.getLoopReturn();
                    } else {
                        Log.fine("We have a full inventory. Let's feed!");
                        localActivity = WintertodtActivity.FEEDING;
                        return Config.getLoopReturn();
                    }
                }

                if (Distance.to(Location.BRUMA_ROOTS_LOCATION) > 4) {
                    Store.setTask("Walking to bruma roots");
                    ExMovement.stepOrRandom(Location.BRUMA_ROOTS_LOCATION);
                    return Config.getLoopReturn();
                }

                GameObject brumaRoots = ExObjects.getNearest(Preds.BRUMA_ROOTS);
                if (brumaRoots != null) {
                    Store.setTask("Chopping roots");
                    Camera.turnTo(brumaRoots);
                    if (!brumaRoots.interact("Chop")) {
                        return Config.getLoopReturn();
                    }

                    sleepWhile(false);
                    return Config.getLoopReturn();
                }

                Log.severe("Can't find roots! Report this.");
                return Config.getLoopReturn();

            case FLETCHING:
                if (WintertodtHelper.isFinalFeedCall()) {
                    Log.fine("We need enough time to feed the brazier! Let's go now.");
                    localActivity = WintertodtActivity.FEEDING;
                    return Config.getLoopReturn();
                }

                if (Distance.to(Location.WEST_BRAZIER_LOCATION) > MIN_BRAZIER_DIST) {
                    Store.setTask("Walking closer to brazier");
                    ExMovement.stepOrRandom(Location.WEST_BRAZIER_LOCATION);
                    return Config.getLoopReturn();
                }

                if (ExInventory.contains(Preds.BRUMA_ROOT)) {
                    Store.setTask("Fletching");
                    if (!Inventory.selectedItem().valid()) {
                        ExInventory.getFirst(Material.KNIFE.getInventoryPred()).interact("Use");
                        Time.sleep(50, 150);
                    }

                    ExInventory.getFirst(Preds.BRUMA_ROOT).interact(a -> true);
                    sleepWhile(false);
//                    if (Time.sleepUntil(() -> Players.local().animation() > 0, 1500)) {
//
//                    } else {
//                        Log.info("Failed to start fletching");
//                    }
                    return Config.getLoopReturn();
                }

                Log.fine("Done fletching. Let's feed the brazier!");
                localActivity = WintertodtActivity.FEEDING;
                return Config.getLoopReturn();

            case FEEDING:
                if (ExInventory.contains(Preds.BRUMA_ROOT) || ExInventory.contains(Preds.BRUMA_KINDLING)) {
                    if (Distance.to(Location.WEST_BRAZIER_LOCATION) > MIN_BRAZIER_DIST) {
                        Store.setTask("Stepping to brazier");
                        ExMovement.stepOrRandom(Location.WEST_BRAZIER_LOCATION);
                        return Config.getLoopReturn();
                    }

                    Store.setTask("Feeding brazier");
                    Camera.turnTo(brazier);

                    if (!brazier.interact(a -> true)) {
                        Log.info("Failed to feed");
                        return Config.getLoopReturn();
                    }

                    sleepWhile(true);
                    return Config.getLoopReturn();
                }

                Log.fine("No more items to feed! Let's go chop more.");
                localActivity = WintertodtActivity.CHOPPING;
                return Config.getLoopReturn();
        }

        return Config.getLoopReturn();
    }

    private void sleepWhile(boolean checkBrazier) {
        long startTime = System.currentTimeMillis();
        int startingHealth = Players.local().healthPercent();

        Log.info("Starting sleep");
        while (shouldContinueToSleep(startingHealth, checkBrazier)) {
            if (Players.local().healthPercent() > startingHealth) {
                startingHealth = Players.local().healthPercent();
            }
            if (Players.local().animation() > 0) {
                startTime = System.currentTimeMillis();
            } else if (System.currentTimeMillis() - startTime > SLEEP_THRESH_MS) {
                break;
            }
        }

        Log.info("Exiting sleep");
    }

    private boolean shouldContinueToSleep(int startingHealth, boolean checkBrazier) {
        if (Config.isStopping()) {
            Log.info("Stopping");
            return false;
        }
        if (Chat.canContinue()) {
            Log.info("Chat popped up");
            return false;
        }
        if (ExObjects.getNearest(Preds.DIRECT_SNOW_FALL, 4) != null) {
            Log.info("Direct snow fall incoming!");
            return false;
        }
        int healthPercent = Players.local().healthPercent();
        if (healthPercent < startingHealth || healthPercent < Store.getEatAtPercent()) {
            Log.info("We got hit! Health went down.");
            return false;
        }
        if (localActivity == WintertodtActivity.CHOPPING && ExInventory.isFull()) {
            Log.info("Inventory is full");
            return false;
        }
        if (localActivity != WintertodtActivity.FEEDING && WintertodtHelper.isFinalFeedCall()) {
            Log.info("Final feed call");
            return false;
        }
        if (checkBrazier && brazierDied()) {
            Log.info("Brazier died");
            return false;
        }
        return true;
    }

    private boolean brazierDied() {
        GameObject brazier = ExObjects.getNearest(Preds.SW_BRAZIER);
        if (brazier == null) {
            return false;
        }
        if (Distance.to(Location.WEST_BRAZIER_LOCATION) > MIN_BRAZIER_DIST) {
            return false;
        }
        return brazier.actions().contains("Light") || brazier.actions().contains("Fix");
    }

    enum WintertodtActivity {
        STARTING,
        FEEDING,
        CHOPPING,
        FLETCHING
    }
}
