package data;

import api.forks.game.utility.Distance;
import org.powbot.api.Tile;
import org.powbot.api.rt4.GameObject;
import org.powbot.api.rt4.Item;
import org.powbot.api.rt4.Movement;
import org.powbot.api.rt4.Players;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class Preds {
    // Brazier > Light, Fix
    // Burning brazier > Feed
    // Bruma roots > Chop
    // Bruma root
    // Bruma kindling

    public static final Predicate<GameObject> BANK_CHEST = obj -> obj.name().equals("Bank chest");
    public static final Predicate<GameObject> DOORS_OF_DINH = obj -> obj.name().equals("Doors of Dinh");
    public static final Predicate<GameObject> SW_BRAZIER = obj -> obj.name().toLowerCase().contains("brazier") && Distance.fromTo(obj, new Tile(1621, 3998)) <= 2;
//            && obj.tile().x() == 1621 && obj.tile().y() == 3998;

    public static final Predicate<GameObject> DIRECT_SNOW_FALL = obj -> obj.id() == 26690 && obj.tile().equals(Players.local().tile());
    public static final Predicate<GameObject> SURROUNDING_SNOW_FALL = obj -> obj.id() == 26690 && Distance.to(obj.tile()) < 10;

    public static final Predicate<GameObject> BRUMA_ROOTS = obj -> obj.name().equals("Bruma roots") && obj.tile().x() == 1621 && obj.tile().y() == 3989;
    public static final Predicate<Item> BRUMA_ROOT =  item -> item.name().equals("Bruma root");
    public static final Predicate<Item> BRUMA_KINDLING =  item -> item.name().equals("Bruma kindling");

    public static final Predicate<Item> SUPPLY_CRATE = item -> item.name().equals("Supply crate");

    private static final List<String> JUNK_ITEM_NAMES = Arrays.asList("Jug", "Vial");
    public static final Predicate<Item> JUNK_ITEM = item -> JUNK_ITEM_NAMES.contains(item.name());


//    public static final Predicate<Item> FOOD = item -> Store.getFood().getInventoryPred().test(item);
//    public static final Predicate<Item> KNIFE =  item -> item.name().equals("Knife");
//    public static final Predicate<Item> TINDERBOX =  item -> item.name().equals("Tinderbox");
//    public static final Predicate<Item> AXE = item -> item.name().toLowerCase().endsWith("axe");

}
