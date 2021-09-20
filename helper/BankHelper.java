package helper;

import api.forks.common.Log;
import api.forks.game.ExEquipment;
import api.forks.game.ExInventory;
import data.Material;
import org.powbot.api.rt4.Inventory;
import org.powbot.api.rt4.Item;
import store.Store;

import java.util.ArrayList;
import java.util.List;

public class BankHelper {

    public static Material getNeededMaterial() {
        for (Material neededMaterial : Store.getNeededMaterials()) {
            int currentCount = currentCount(neededMaterial);
            int neededCount = neededMaterial.getAmount() - currentCount;
            if (neededCount > 0) {
                neededMaterial.setCurrentAmountNeeded(neededCount);
                return neededMaterial;
            }
        }
        return null;
    }

    public static List<Material> getNeededMaterials() {
        List<Material> materials = new ArrayList<>();
        for (Material neededMaterial : Store.getNeededMaterials()) {
            int currentCount = currentCount(neededMaterial);
            int neededCount = neededMaterial.getAmount() - currentCount;
            if (neededCount > 0) {
                neededMaterial.setCurrentAmountNeeded(neededCount);
                materials.add(neededMaterial);
            }
        }
        return materials;
    }

    private static int currentCount(Material material) {
        switch (material.getCheckAgainst()) {
            case INVENTORY:
                return (int) ExInventory.getCount(material.getInventoryPred());

            case EQUIPMENT:
                return (int) ExEquipment.getCount(material.getEquipmentPred());

            case INVENTORY_AND_EQUIPMENT:
                return (int) ExInventory.getCount(material.getInventoryPred())
                        + (int) ExEquipment.getCount(material.getEquipmentPred());
        }
        return 0;
    }

    public static Item getUneededInventoryItem() {
        for (Item item : Inventory.get()) {
            boolean needed = false;
            for (Material neededMaterial : Store.getNeededMaterials()) {
                if (neededMaterial.getInventoryPred() == null) {
                    Log.info("Inventory pred null for " + neededMaterial.getName());
                    continue;
                }
                if (neededMaterial.getInventoryPred().test(item)
                        && ExInventory.getCount(item.id()) <= neededMaterial.getAmount()) {
                    needed = true;
                    break;
                }
            }
            if (!needed) {
                return item;
            }
        }
        return null;
    }


}
