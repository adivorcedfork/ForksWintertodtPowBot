package data;

import org.powbot.api.rt4.Item;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public enum Material {
    AXE(Arrays.asList("Axe", "Bruma torch"), 1,
            item -> item.name().endsWith("axe") || item.name().equals("Bruma torch"),
            item -> !item.noted() && (item.name().endsWith("axe") || item.name().equals("Bruma torch")),
            item -> item.name().endsWith("axe") || item.name().equals("Bruma torch"),
            CheckAgainst.INVENTORY_AND_EQUIPMENT),

    KNIFE(Collections.singletonList("Knife"), 1,
            item -> item.name().equals("Knife"),
            item -> !item.noted() && item.name().equals("Knife"),
            null,
            CheckAgainst.INVENTORY),

    TINDERBOX(Collections.singletonList("Tinderbox"), 1,
            item -> item.name().equals("Tinderbox"),
            item -> !item.noted() && item.name().equals("Tinderbox"),
            null,
            CheckAgainst.INVENTORY),

    // Placeholder
    FOOD(Collections.singletonList("Food"), 0,
            item -> item.name().equals("Food"),
            item -> !item.noted() && item.name().equals("Food"),
            item -> item.name().equals("Food"),
            CheckAgainst.INVENTORY),
    ;

    // Specifies what to check inventory pred against when confirming we have x material
    public enum CheckAgainst {
        INVENTORY,
        EQUIPMENT,
        INVENTORY_AND_EQUIPMENT
    }

    private List<String> names;
    private int amount;
    private Predicate<Item> bankPred;
    private Predicate<Item> inventoryPred;
    private Predicate<Item> equipmentPred;
    private CheckAgainst checkAgainst;
    private int currentAmountNeeded;

    Material(List<String> names, int amount, Predicate<Item> bankPred,
             Predicate<Item> inventoryPred, Predicate<Item> equipmentPred, CheckAgainst checkAgainst) {
        this.names = names;
        this.amount = amount;
        this.bankPred = bankPred;
        this.inventoryPred = inventoryPred;
        this.equipmentPred = equipmentPred;
        this.checkAgainst = checkAgainst;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setFoodPreds() {
        this.bankPred = item -> names.contains(item.name());
        this.inventoryPred = item -> !item.noted() && names.contains(item.name());
    }

    public Predicate<Item> getInventoryPred() {
        return inventoryPred;
    }

    public void setInventoryPred(Predicate<Item> inventoryPred) {
        this.inventoryPred = inventoryPred;
    }

    public Predicate<Item> getBankPred() {
        return bankPred;
    }

    public void setBankPred(Predicate<Item> bankPred) {
        this.bankPred = bankPred;
    }

    public CheckAgainst getCheckAgainst() {
        return checkAgainst;
    }

    public void setCheckAgainst(CheckAgainst checkAgainst) {
        this.checkAgainst = checkAgainst;
    }
    public Predicate<Item> getEquipmentPred() {
        return equipmentPred;
    }

    public void setEquipmentPred(Predicate<Item> equipmentPred) {
        this.equipmentPred = equipmentPred;
    }

    public int getCurrentAmountNeeded() {
        return currentAmountNeeded;
    }

    public void setCurrentAmountNeeded(int currentAmountNeeded) {
        this.currentAmountNeeded = currentAmountNeeded;
    }

    public String getName() {
        return names.get(0);
    }

}
