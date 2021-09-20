package api.forks.game;

import org.powbot.api.rt4.Equipment;
import org.powbot.api.rt4.Inventory;
import org.powbot.api.rt4.Item;

import java.util.List;
import java.util.function.Predicate;

public class ExEquipment {

    public static boolean contains(Predicate<Item> pred) {
        return !Equipment.stream().filter(pred).isEmpty();
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

    public static long getCount(String... names) {
        return sumItems(Equipment.stream().name(names).list());
    }

    public static long getCount(int... ids) {
        return sumItems(Equipment.stream().id(ids).list());
    }

    public static long getCount(Predicate<Item> pred) {
        return sumItems(Equipment.stream().filter(pred).list());
    }
}
