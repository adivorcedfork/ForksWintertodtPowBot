package helper;

import api.forks.common.Log;
import api.forks.common.Random;
import api.forks.common.Time;
import api.forks.game.ExMovement;
import api.forks.game.utility.Distance;
import data.Preds;
import org.powbot.api.Area;
import org.powbot.api.Tile;
import org.powbot.api.rt4.GameObject;
import org.powbot.api.rt4.Movement;
import org.powbot.api.rt4.Objects;
import org.powbot.api.rt4.Players;
import store.Store;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MovementHelper {

    private static final int LOWER_RUN_THRESH = 5;
    private static final int UPPER_RUN_THRESH = 15;
    private static int runThreshold = Random.nextInt(LOWER_RUN_THRESH, UPPER_RUN_THRESH);

    private static final int LOWER_DIST_THRESH = 3;
    private static final int UPPER_DIST_THRESH = 10;
    private static int distanceThreshold = Random.nextInt(LOWER_DIST_THRESH, UPPER_DIST_THRESH);

    public static boolean needsToRun() {
        if (!Movement.running() && Movement.energyLevel() > runThreshold) {
            runThreshold = Random.nextInt(LOWER_RUN_THRESH, UPPER_RUN_THRESH);
            return true;
        }
        return false;
    }

    public static boolean isFarFromDestination() {
        if (Players.local().valid() && Players.local().inMotion() && ExMovement.destinationDistance() > distanceThreshold) {
            distanceThreshold = Random.nextInt(LOWER_DIST_THRESH, UPPER_DIST_THRESH);
            return true;
        }
        return false;
    }

    public static boolean avoidSnowfall() {
        List<GameObject> snowfalls = Objects.stream()
                .filter(Preds.SURROUNDING_SNOW_FALL).list();

        List<Tile> blacklistedTiles = new ArrayList<>();
        for (GameObject snowfall : snowfalls) {
            blacklistedTiles.add(snowfall.tile());
        }

        Tile myTile = Players.local().tile();
        Area escapeArea = new Area(myTile.derive(-2, 2), myTile.derive(2, -2));

        Tile bestTile = null;
        double bestPosDistance = -1;
        for (Tile tile : escapeArea.getTiles()) {
            if (ExMovement.reachable(tile) && !blacklistedTiles.contains(tile) &&
                    (bestTile == null || Distance.to(tile) < bestPosDistance)) {
                bestTile = tile;
                bestPosDistance = Distance.to(tile);
            }
        }
        if (bestTile == null) {
            Log.severe("Could not find a tile to escape to! Guess we're going to get hit.");
            return false;
        }

        Log.info("Walking to best tile to avoid snowfall: " + bestTile.x() + ", " + bestTile.y());
        boolean stepped = ExMovement.stepOrRandom(bestTile);

        Tile finalBestTile = bestTile;
        return Time.sleepUntil(() -> !stepped
                || (Players.local().tile().equals(finalBestTile)
                && Objects.stream().filter(Preds.SURROUNDING_SNOW_FALL).isEmpty())
                ,6000);
    }
}
