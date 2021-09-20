package data;

import java.util.Arrays;

public enum Food {
    JUG_OF_WINE("Jug of wine"),
    CAKE("Cake", "2/3 cake", "Slice of cake");

    private String[] names;

    Food(String... names) {
        this.names = names;
    }

    public static Food fromName(String name) {
        for (Food food : Food.values()) {
            if (food.names[0].equals(name)) {
                return food;
            }
        }
        return null;
    }

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }


}
