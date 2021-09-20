import api.forks.common.Log;
import api.forks.common.Time;
import data.Food;
import data.Location;
import data.Material;
import executor.BankingExecutor;
import executor.WintertodtExecutor;
import helper.MovementHelper;
import org.powbot.api.rt4.Game;
import org.powbot.api.rt4.Movement;
import org.powbot.api.rt4.Players;
import org.powbot.api.rt4.walking.model.Skill;
import org.powbot.api.script.AbstractScript;
import org.powbot.api.script.OptionType;
import org.powbot.api.script.ScriptConfiguration;
import org.powbot.api.script.ScriptManifest;
import org.powbot.api.script.paint.Paint;
import org.powbot.api.script.paint.PaintBuilder;
import org.powbot.mobile.script.ScriptManager;
import org.powbot.mobile.service.ScriptUploader;
import store.Config;
import store.Store;

import java.io.IOException;
import java.util.Arrays;

@ScriptManifest(
        name = "Fork's Wintertodt",
        description = "Does Wintertodt n stuff",
        version = "0.0.1",
        priv = true
)

@ScriptConfiguration.List(
        {
                @ScriptConfiguration(
                        name = "Food",
                        description = "Food to eat",
                        optionType = OptionType.STRING,
                        defaultValue = "Jug of wine",
                        allowedValues = {"Jug of wine", "Cake"}
                ),
                @ScriptConfiguration(
                        name = "Food Amount",
                        description = "Food Amount to withdraw when banking",
                        optionType = OptionType.INTEGER,
                        defaultValue = "10"
                ),
                @ScriptConfiguration(
                        name = "Min Food Amount",
                        description = "If we go under this amount of food, the script will bank for more",
                        optionType = OptionType.INTEGER,
                        defaultValue = "3"
                ),
                @ScriptConfiguration(
                        name = "Eat At Percent",
                        description = "Script will eat when player health is at or below this percent (1-99)",
                        optionType = OptionType.INTEGER,
                        defaultValue = "70"
                ),
                @ScriptConfiguration(
                        name = "Max Crates",
                        description = "If we go over this amount of crates, we will bank even if we still have food",
                        optionType = OptionType.INTEGER,
                        defaultValue = "3"
                ),
                @ScriptConfiguration(
                        name = "Should Fletch",
                        description = "Should we fletch logs before burning them?",
                        optionType = OptionType.BOOLEAN,
                        defaultValue = "true"
                )
        }
)

public class Wintertodt extends AbstractScript {

    private BankingExecutor bankingExecutor = new BankingExecutor();
    private WintertodtExecutor wintertodtExecutor = new WintertodtExecutor();

    public static void main(String[] args) throws IOException {
        ProcessBuilder builder = new ProcessBuilder("C:\\Users\\Eleven\\.powbot\\android\\platform-tools\\adb.exe", "-s", "127.0.0.1:5559", "forward", "tcp:61666", "tcp:61666");
        builder.start();
        new ScriptUploader().uploadAndStart("Fork's Wintertodt", "", null, false, true);
    }

    @Override
    public void onStart() {
        Log.fine("Starting " + getManifest().name() + "!");

        Paint p = new PaintBuilder()
                .trackSkill(Skill.Firemaking)
                .trackSkill(Skill.Woodcutting)
                .x(30)
                .y(65)
                .build();

        addPaint(p);

        setGUIValues();
    }

    private void setGUIValues() {
        Object foodObj = getOption("Food");
        Object foodAmountObj = getOption("Food Amount");
        Object minFoodAmountObj = getOption("Min Food Amount");
        Object eatAtPercentObj = getOption("Eat At Percent");
        Object maxCratesObj = getOption("Max Crates");
        Object shouldFletchObj = getOption("Should Fletch");

        if (foodObj == null || foodAmountObj == null || minFoodAmountObj == null
                || eatAtPercentObj == null || maxCratesObj == null || shouldFletchObj == null) {
            Log.severe("GUI value null");
            Config.setStopping(true);
            return;
        }

        Store.setFood(Food.fromName(String.valueOf(foodObj)));
        Store.setFoodAmount((int) foodAmountObj);
        Store.setMinFoodAmount((int) minFoodAmountObj);
        Store.setEatAtPercent((int) eatAtPercentObj);
        Store.setMaxCrates((int) maxCratesObj);
        Store.setShouldFletch((boolean) shouldFletchObj);

        Material.FOOD.setNames(Arrays.asList(Store.getFood().getNames()));
        Material.FOOD.setAmount(Store.getFoodAmount());
        Material.FOOD.setFoodPreds();
    }

    @Override
    public void poll() {
        Time.sleep(loop());
    }

    // Because I like to return my sleep...
    public int loop() {
        // GLOBAL CONDITIONS (start)
        if (!Config.isSetup()) {
            Store.setTask("Setting up");
            Time.sleepUntil(() -> Config.isSetup() || ScriptManager.INSTANCE.isStopping(), 20000);
            return Config.getLoopReturn();
        }
        if (Config.isStopping()) {
            Store.setTask("Stopping");
            this.controller.stop();
            return Config.getLoopReturn();
        }

        if (!Game.loggedIn()) {
            Store.setTask("Not logged in");
            return Config.getLoopReturn();
        }
        if (Location.LUMBRIDGE_SPAWN.contains(Players.local())) {
            Log.severe("We died! Stopping script...");
            Config.setStopping(true);
            return Config.getLoopReturn();
        }
        if (MovementHelper.needsToRun()) {
            Store.setTask("Enabling run");
            Movement.running(true);
            return Config.getLoopReturn();
        }
        if (Game.tab() != Game.Tab.INVENTORY) {
            Store.setTask("Opening inventory");
            Game.tab(Game.Tab.INVENTORY);
            return Config.getLoopReturn();
        }
        // GLOBAL CONDITIONS (end)

        // ACTIVITIES (start)
        switch (Store.getActivity()) {
            case BANKING:
                return bankingExecutor.execute();
            case WINTERTODT:
                return wintertodtExecutor.execute();
        }
        // ACTIVITIES (end)

        Store.setTask("Unknown Activity. Idling...");
        return Config.getLoopReturn();
    }

    @Override
    public void onStop() {
        Config.setStopping(true);
        Log.fine("Stopping");
        Log.fine("Thanks for using Fork's " + getManifest().name() + "!");
    }
}
