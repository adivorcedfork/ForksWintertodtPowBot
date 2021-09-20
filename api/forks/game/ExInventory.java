package api.forks.game;

import org.powbot.api.rt4.Inventory;
import org.powbot.api.rt4.Item;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class ExInventory {

    public static boolean contains(int... ids) {
        return !Inventory.stream().id(ids).isEmpty();
    }

    public static boolean contains(String... names) {
        return !Inventory.stream().name(names).isEmpty();
    }

    public static boolean contains(Predicate<Item> pred) {
        return !Inventory.stream().filter(pred).isEmpty();
    }

    public static boolean containsAll(int... ids) {
        for (int id : ids) {
            if (!Inventory.stream().id(id).first().valid()) {
                return false;
            }
        }
        return true;
    }

    public static boolean containsAll(String... names) {
        for (String name : names) {
            if (!Inventory.stream().name(name).first().valid()) {
                return false;
            }
        }
        return true;
    }

    public static boolean containsOnly(int... ids) {
        final Predicate<Item> INVALID_ITEM = item ->
                Arrays.stream(ids).noneMatch(i -> i == item.id());
        return Inventory.stream().noneMatch(INVALID_ITEM);
    }

    public static boolean containsOnly(String... names) {
        final Predicate<Item> INVALID_ITEM = item ->
                Arrays.stream(names).noneMatch(n -> n.equals(item.name()));
        return Inventory.stream().noneMatch(INVALID_ITEM);
    }

    public static boolean containsOnly(Predicate<Item> pred) {
        return Inventory.stream().allMatch(pred);
    }

    private static long sumItems(List<Item> items) {
        long count = 0;
        for (Item item : items) {
            if (item.stackable()) {
                count += item.stackSize();
            } else {
                count++;
            }
        }
        return count;
    }

    public static long getCount() {
        return sumItems(Inventory.stream().list());
    }

    public static long getCount(String... names) {
        return sumItems(Inventory.stream().name(names).list());
    }

    public static long getCount(int... ids) {
        return sumItems(Inventory.stream().id(ids).list());
    }

    public static long getCount(Predicate<Item> pred) {
        return sumItems(Inventory.stream().filter(pred).list());
    }

    public static Item getFirst(String name) {
        return Inventory.stream().name(name).firstOrNull();
    }

    public static Item getFirst(int... ids) {
        return Inventory.stream().id(ids).firstOrNull();
    }

    public static Item getFirst(Predicate<Item> pred) {
        return Inventory.stream().filter(pred).firstOrNull();
    }

    public static boolean isEmpty() {
        return Inventory.stream().isEmpty();
    }

    public static boolean isFull() {
        return Inventory.isFull();
    }
}
