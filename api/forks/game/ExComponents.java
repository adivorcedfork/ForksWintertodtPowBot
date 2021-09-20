package api.forks.game;

import org.powbot.api.rt4.Component;
import org.powbot.api.rt4.Components;

import java.util.function.Predicate;

public class ExComponents {

    public static Component getFirst(int parent, Predicate<Component> pred) {
        return Components.stream(parent).filter(pred).firstOrNull();
    }
}
