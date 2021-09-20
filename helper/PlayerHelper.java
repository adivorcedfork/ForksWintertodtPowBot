package helper;

import api.forks.common.Log;
import api.forks.game.ExEquipment;
import api.forks.game.ExInventory;
import data.Material;
import data.Preds;
import store.Store;

public class PlayerHelper {
    public static boolean hasRequiredItems() {
        if (!ExInventory.containsAll("Knife", "Tinderbox")) {
            Log.info("Needs knife or tinderbox");
            return false;
        }
        if (!ExInventory.contains(Material.AXE.getInventoryPred())
                && !ExEquipment.contains(Material.AXE.getEquipmentPred())) {
            Log.info("Needs axe");
            return false;
        }
        if (ExInventory.getCount(Material.FOOD.getInventoryPred()) <= Store.getMinFoodAmount()) {
            Log.info("Not enough food");
            return false;
        }
        if (ExInventory.getCount(Preds.SUPPLY_CRATE) >= Store.getMaxCrates()) {
            Log.info("Too many supply crates");
            return false;
        }
        return true;
    }
}
