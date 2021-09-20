package data;

import org.powbot.api.Area;
import org.powbot.api.Locatable;
import org.powbot.api.Tile;

public class Location {
    public static final Area LUMBRIDGE_SPAWN = new Area(new Tile(3211, 3212), new Tile(3228, 3227));

    public static final Area WINTERTODT_AREA = new Area(new Tile(1577, 3966), new Tile(1686, 4040));
    public static final Tile WINTERTODT_EXIT = new Tile(1630, 3969, 0);
    public static final Tile WINTERTODT_ENTRANCE = new Tile(1630, 3962, 0);
    public static final Tile WINTERTODT_BANK = new Tile(1639, 3944, 0);

    public static final Tile BRUMA_ROOTS_LOCATION = new Tile(1621, 3990, 0);
    public static final Tile WEST_BRAZIER_LOCATION = new Tile(1621, 3996, 0);
}
