package api.forks.game;

import api.forks.common.Log;
import api.forks.common.Time;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Chat;
import org.powbot.api.rt4.ChatOption;
import org.powbot.api.rt4.Item;

import java.util.function.Predicate;

public class ExBank {
    public static boolean deposit(Predicate<Item> pred, Bank.Amount amount) {
        Item item = Bank.stream().filter(pred).firstOrNull();
        if (item == null) {
            Log.info("Pred not found. Can't deposit.");
            return false;
        }
        return Bank.deposit(item.id(), amount);
    }

    public static boolean contains(Predicate<Item> pred) {
        return Bank.stream().filter(pred).firstOrNull() != null;
    }

    public static boolean contains(String name) {
        return Bank.stream().name(name).firstOrNull() != null;
    }

    public static boolean contains(int id) {
        return Bank.stream().id(id).firstOrNull() != null;
    }

    public static boolean withdraw(Predicate<Item> pred, int amount) {
        Item item = Bank.stream().filter(pred).firstOrNull();
        if (item == null) {
            Log.info("Pred not found. Can't withdraw.");
            return false;
        }

        Bank.withdrawAmount(item.id(), amount);
        return true;

//        Bank.withdrawAmount(item.id(), Bank.Amount.X);
//        boolean opened = Time.sleepUntil(Chat::pendingInput, 3000);
//        if (!opened) {
//            return false;
//        }
//        return Chat.sendInput(amount);
    }

    public static long getCount(Predicate<Item> pred) {
        return Bank.stream().filter(pred).first().stackSize();
    }
}
