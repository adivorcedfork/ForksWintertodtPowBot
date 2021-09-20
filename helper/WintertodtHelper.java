package helper;

import api.forks.game.ExComponents;
import api.forks.game.ExInventory;
import api.forks.util.NumberUtil;
import data.Preds;
import org.powbot.api.rt4.Component;

public class WintertodtHelper {

    private static final int WINTERTODT_STATUS_PARENT = 396;

    public static int getEnergy() {
        Component energyComp = ExComponents.getFirst(WINTERTODT_STATUS_PARENT, comp ->
                comp.text().startsWith("Wintertodt's Energy")); // 21

        if (energyComp != null) {
            return NumberUtil.numbersOnly(energyComp.text());
        }
        return 0;
    }

    public static int getSecondsUntilReturn() {
        Component energyComp = ExComponents.getFirst(WINTERTODT_STATUS_PARENT, comp ->
                comp.text().startsWith("The Wintertodt returns")); // 3

        if (energyComp != null) {
            int num = NumberUtil.numbersOnly(energyComp.text());
            if (num >= 100) {
                return (num - 100) + 60;
            }
            return num;
        }
        return 0;
    }

    public static int getCurrentPoints() {
        Component energyComp = ExComponents.getFirst(WINTERTODT_STATUS_PARENT, comp ->
                comp.text().startsWith("Points")); // 7

        if (energyComp != null) {
            return NumberUtil.numbersOnly(energyComp.text());
        }
        return 0;
    }

    public static boolean isFinalFeedCall() {
        return WintertodtHelper.getEnergy() <= (ExInventory.getCount(Preds.BRUMA_ROOT) + ExInventory.getCount(Preds.BRUMA_KINDLING));
    }
}
