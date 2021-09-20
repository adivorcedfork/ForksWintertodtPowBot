package api.forks.game.utility;

import org.powbot.api.Locatable;
import org.powbot.api.rt4.Players;

public class Distance {

    public static int to(Locatable l1) {
        Locatable l2 = Players.local().tile();
        return fromTo(l1, l2);
    }

    public static int fromTo(Locatable l1, Locatable l2) {
        return (int) Math.hypot(l1.tile().x() - l2.tile().x(), l1.tile().y() - l2.tile().y());
    }
}
