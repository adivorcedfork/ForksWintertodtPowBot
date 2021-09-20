package executor;

import api.forks.common.Log;
import api.forks.common.Time;
import api.forks.game.*;
import api.forks.game.utility.Distance;
import data.Activity;
import data.Location;
import data.Material;
import data.Preds;
import helper.BankHelper;
import org.powbot.api.rt4.*;
import store.Config;
import store.Stats;
import store.Store;

public class BankingExecutor extends ActivityExecutor {

    @Override
    public int execute() {
        if (Bank.opened()) {
            if (!Bank.present()) {
                Store.setTask("Loading bank");
                return Config.getLoopReturn();
            }

            Item unneededItem = BankHelper.getUneededInventoryItem();
            if (unneededItem != null) {
                Store.setTask("Depositing unneeded items");
                long supplyCrateCount = 0;
                if (unneededItem.name().equals("Supply crate")) {
                    supplyCrateCount = ExInventory.getCount("Supply crate");
                }
                Bank.depositInventory();
                boolean success = Time.sleepUntil(ExInventory::isEmpty, 2000);
                if (success) {
                    Stats.addCrates((int) supplyCrateCount);
                }
                return Config.getLoopReturn();
            }

            if (Bank.withdrawModeNoted()) {
                Store.setTask("Setting withdraw mode to ITEM");
                Bank.withdrawModeNoted(false);
                return Config.getLoopReturn();
            }

            Material neededMaterial = BankHelper.getNeededMaterial();
            if (neededMaterial != null) {
                Store.setTask("Withdrawing " + neededMaterial.getName());
                if (!ExBank.contains(neededMaterial.getBankPred())) {
                    Log.severe("Can't find " + neededMaterial.getName() + "! Stopping...");
                    Config.setStopping(true);
                    return Config.getLoopReturn();
                }

                ExBank.withdraw(neededMaterial.getBankPred(), neededMaterial.getCurrentAmountNeeded());
                return Config.getLoopReturn();
            }

            Store.setTask("Closing bank");
            Bank.close();
            return Config.getLoopReturn();
        }

        if (BankHelper.getNeededMaterial() != null || BankHelper.getUneededInventoryItem() != null) {
            if (Chat.stream().count() > 0) {
                Store.setTask("Processing dialog");
                boolean success = Chat.continueChat("Leave and lose all progress.");
                if (!success) Chat.sendInput(0);
                return Config.getLoopReturn();
            }

            GameObject exit = ExObjects.getNearest(Preds.DOORS_OF_DINH);
            if (Location.WINTERTODT_AREA.contains(Players.local())) {
                if (exit != null && Distance.to(Location.WINTERTODT_EXIT) < 5) {
                    Store.setTask("Exiting wintertodt");
                    Camera.turnTo(exit);
                    if (!exit.interact(a->true)) {
                        Log.severe("Failed to interact with exit");
                        return Config.getLoopReturn();
                    }
                    Time.sleepUntil(() -> !Players.local().inMotion(), 15000);
                    Time.sleepUntil(() -> !Location.WINTERTODT_AREA.contains(Players.local()), 3000);
                    return Config.getLoopReturn();
                }

                Store.setTask("Walking to exit");
                ExMovement.stepOrRandom(Location.WINTERTODT_EXIT);
                return Config.getLoopReturn();
            }

            GameObject bank = ExObjects.getNearest(Preds.BANK_CHEST);
            if (bank == null || Distance.to(bank) > 5) {
                Store.setTask("Moving to bank");
                boolean walked = Movement.walkTo(Location.WINTERTODT_BANK);
                if (!walked) {
                    Log.severe("Please move closer to Wintertodt bank. Bank is " + (bank != null ? "found" : "not found"));
                    return Config.getLoopReturn();
                }
                return Config.getLoopReturn();
            }

            Store.setTask("Opening bank");
            Log.info("Distance to bank: " + Distance.to(bank));
            Camera.turnTo(Bank.nearest());
            Bank.open();
            Time.sleepUntil(Bank::opened, 2500);
            return Config.getLoopReturn();
        }

        Item axe = ExInventory.getFirst(Material.AXE.getInventoryPred());
        if (axe != null && !ExEquipment.contains(Material.AXE.getInventoryPred())) {
            Store.setTask("Equipping axe");
            if (!axe.interact("Wield")) {
                Log.severe("Failed to wield axe");
                return Config.getLoopReturn();
            }
            Time.sleepUntil(() -> ExEquipment.contains(Material.AXE.getInventoryPred()), 1500);
            return Config.getLoopReturn();
        }

        if (!Location.WINTERTODT_AREA.contains(Players.local())) {
            GameObject entrance = ExObjects.getNearest(Preds.DOORS_OF_DINH);
            if (entrance != null && Distance.to(Location.WINTERTODT_ENTRANCE) < 5) {
                Store.setTask("Entering wintertodt " + Distance.to(entrance));
                Camera.turnTo(entrance);
                if (!entrance.interact(a->true)) {
                    Log.severe("Failed to interact with entrance");
                    return Config.getLoopReturn();
                }
                Time.sleepUntil(() -> !Players.local().inMotion(), 15000);
                Time.sleepUntil(() -> Location.WINTERTODT_AREA.contains(Players.local()), 3000);
                return Config.getLoopReturn();
            }

            Store.setTask("Walking to entrance");
            ExMovement.stepOrRandom(Location.WINTERTODT_ENTRANCE);
            return Config.getLoopReturn();
        }

        Log.fine("We have all items and are ready for the next game! Let's get ready...");
        Store.setActivity(Activity.WINTERTODT);
        return Config.getLoopReturn();
    }
}
