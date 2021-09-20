package api.forks.game;

import api.forks.common.Log;
import api.forks.common.Random;
import org.powbot.api.Locatable;
import org.powbot.api.rt4.Movement;
import org.powbot.api.rt4.Players;
import org.powbot.api.rt4.WebWalking;

public class ExMovement {

    public static boolean stepOrRandom(Locatable locatable) {
        boolean success = Movement.step(locatable);
        if (!success) {
            Log.info("Failed to step. Walking to random tile.");
            randomWalk();
        }
        return success;
    }

    public static int destinationDistance() {
        return Movement.distance(Movement.destination());
    }

    public static boolean reachable(Locatable l) {
        return Movement.reachable(Players.local().tile(), l);
    }

    public static boolean randomWalk() {
        return Movement.step(Players.local().tile().derive(Random.nextInt(-2, 3), Random.nextInt(-2, 3)));
    }
}
