package api.forks.game;

import org.powbot.api.rt4.Game;
import org.powbot.api.rt4.GameObject;
import org.powbot.api.rt4.Objects;

import java.util.List;
import java.util.function.Predicate;

public class ExObjects {

    public static GameObject getNearest(Predicate<GameObject> pred) {
        return Objects.stream().filter(pred).nearest().firstOrNull();
    }

    public static GameObject getNearest(Predicate<GameObject> pred, int maxDistance) {
        return Objects.stream().filter(pred).filter(obj -> obj.tile().distance() <= maxDistance).nearest().firstOrNull();
    }

    public static List<GameObject> getLoaded(Predicate<GameObject> pred) {
        return Objects.stream().filter(pred).list();
    }
}
