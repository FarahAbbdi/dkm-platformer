import java.util.Properties;

/**
 * Factory class for creating game objects for both levels.
 * Uses level number to determine which objects to create and property keys to use.
 */
public class GameObjectFactory {
    private final Properties gameProps;
    private final int levelNumber;
    private final String levelKey; // "level1" or "level2"

    /**
     * Creates a new GameObjectFactory for the specified level.
     *
     * @param gameProps The properties containing game configuration
     * @param levelNumber The level number (1 or 2)
     */
    public GameObjectFactory(Properties gameProps, int levelNumber) {
        this.gameProps = gameProps;
        this.levelNumber = levelNumber;
        this.levelKey = "level" + levelNumber;
    }

    /**
     * Creates Mario from properties.
     */
    public Mario createMario() {
        String marioPosition = gameProps.getProperty("mario." + levelKey);
        String[] marioCoords = marioPosition.split(",");
        double marioX = Double.parseDouble(marioCoords[0]);
        double marioY = Double.parseDouble(marioCoords[1]);
        return new Mario(marioX, marioY);
    }

    /**
     * Creates Donkey from properties.
     */
    public Donkey createDonkey() {
        String donkeyPosition = gameProps.getProperty("donkey." + levelKey);
        String[] donkeyCoords = donkeyPosition.split(",");
        double donkeyX = Double.parseDouble(donkeyCoords[0]);
        double donkeyY = Double.parseDouble(donkeyCoords[1]);
        return new Donkey(donkeyX, donkeyY);
    }

    /**
     * Creates barrels array from properties.
     */
    public Barrel[] createBarrels() {
        int barrelCount = Integer.parseInt(gameProps.getProperty("barrel." + levelKey + ".count"));
        Barrel[] barrels = new Barrel[barrelCount];

        for (int i = 1; i <= barrelCount; i++) {
            String barrelData = gameProps.getProperty("barrel." + levelKey + "." + i);
            if (barrelData != null) {
                String[] coords = barrelData.split(",");
                if (coords.length >= 2) {
                    double barrelX = Double.parseDouble(coords[0]);
                    double barrelY = Double.parseDouble(coords[1]);
                    barrels[i-1] = new Barrel(barrelX, barrelY);
                }
            }
        }
        return barrels;
    }

    /**
     * Creates ladders array from properties.
     */
    public Ladder[] createLadders() {
        int ladderCount = Integer.parseInt(gameProps.getProperty("ladder." + levelKey + ".count"));
        Ladder[] ladders = new Ladder[ladderCount];

        for (int i = 1; i <= ladderCount; i++) {
            String ladderData = gameProps.getProperty("ladder." + levelKey + "." + i);
            if (ladderData != null) {
                String[] coords = ladderData.split(",");
                if (coords.length >= 2) {
                    double ladderX = Double.parseDouble(coords[0]);
                    double ladderY = Double.parseDouble(coords[1]);
                    ladders[i-1] = new Ladder(ladderX, ladderY);
                }
            }
        }
        return ladders;
    }

    /**
     * Creates platforms array from properties.
     */
    public Platform[] createPlatforms() {
        String platformData = gameProps.getProperty("platforms." + levelKey);
        if (platformData != null && !platformData.isEmpty()) {
            String[] platformEntries = platformData.split(";");
            Platform[] platforms = new Platform[platformEntries.length];
            int pIndex = 0;

            for (String entry : platformEntries) {
                String[] coords = entry.trim().split(",");
                if (coords.length >= 2) {
                    double x = Double.parseDouble(coords[0]);
                    double y = Double.parseDouble(coords[1]);
                    platforms[pIndex] = new Platform(x, y);
                    pIndex++;
                }
            }
            return platforms;
        }
        return new Platform[0];
    }

    /**
     * Creates hammers array from properties.
     */
    public Hammer[] createHammers() {
        int hammerCount = Integer.parseInt(gameProps.getProperty("hammer." + levelKey + ".count"));
        Hammer[] hammers = new Hammer[hammerCount];

        for (int i = 1; i <= hammerCount; i++) {
            String hammerData = gameProps.getProperty("hammer." + levelKey + "." + i);
            if (hammerData != null) {
                String[] hammerCoords = hammerData.split(",");
                if (hammerCoords.length >= 2) {
                    double hammerX = Double.parseDouble(hammerCoords[0]);
                    double hammerY = Double.parseDouble(hammerCoords[1]);
                    hammers[i-1] = new Hammer(hammerX, hammerY);
                }
            }
        }
        return hammers;
    }

    /**
     * Creates blasters array from properties (Level 2 only).
     */
    public Blaster[] createBlasters() {
        if (levelNumber != 2) {
            return null; // Level 1 doesn't have blasters
        }

        int blasterCount = Integer.parseInt(gameProps.getProperty("blaster." + levelKey + ".count"));
        Blaster[] blasters = new Blaster[blasterCount];

        for (int i = 1; i <= blasterCount; i++) {
            String blasterData = gameProps.getProperty("blaster." + levelKey + "." + i);
            if (blasterData != null) {
                String[] blasterCoords = blasterData.split(",");
                double blasterX = Double.parseDouble(blasterCoords[0]);
                double blasterY = Double.parseDouble(blasterCoords[1]);
                blasters[i-1] = new Blaster(blasterX, blasterY);
            }
        }
        return blasters;
    }

    /**
     * Creates normal monkeys array from properties (Level 2 only).
     */
    public NormalMonkey[] createNormalMonkeys() {
        if (levelNumber != 2) {
            return null; // Level 1 doesn't have normal monkeys
        }

        int normalMonkeyCount = Integer.parseInt(gameProps.getProperty("normalMonkey." + levelKey + ".count"));
        NormalMonkey[] normalMonkeys = new NormalMonkey[normalMonkeyCount];

        for (int i = 1; i <= normalMonkeyCount; i++) {
            String monkeyData = gameProps.getProperty("normalMonkey." + levelKey + "." + i);
            if (monkeyData != null) {
                NormalMonkey monkey = parseMonkeyData(monkeyData, true);
                if (monkey != null) {
                    normalMonkeys[i-1] = monkey;
                }
            }
        }
        return normalMonkeys;
    }

    /**
     * Creates intelligent monkeys array from properties (Level 2 only).
     */
    public IntelligentMonkey[] createIntelligentMonkeys() {
        if (levelNumber != 2) {
            return null; // Level 1 doesn't have intelligent monkeys
        }

        int intelligentMonkeyCount = Integer.parseInt(gameProps.getProperty("intelligentMonkey." + levelKey + ".count"));
        IntelligentMonkey[] intelligentMonkeys = new IntelligentMonkey[intelligentMonkeyCount];

        for (int i = 1; i <= intelligentMonkeyCount; i++) {
            String monkeyData = gameProps.getProperty("intelligentMonkey." + levelKey + "." + i);
            if (monkeyData != null) {
                IntelligentMonkey monkey = parseMonkeyData(monkeyData, false);
                if (monkey != null) {
                    intelligentMonkeys[i-1] = monkey;
                }
            }
        }
        return intelligentMonkeys;
    }

    /**
     * Helper method to parse monkey data and create appropriate monkey type.
     *
     * @param monkeyData The monkey configuration string
     * @param isNormalMonkey true for NormalMonkey, false for IntelligentMonkey
     * @return The created monkey object
     */
    private <T> T parseMonkeyData(String monkeyData, boolean isNormalMonkey) {
        String[] parts = monkeyData.split(";");
        if (parts.length >= 3) {
            // Parse position
            String[] coords = parts[0].split(",");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);

            // Parse direction
            String direction = parts[1];

            // Parse waypoints
            String[] waypointStrings = parts[2].split(",");
            int[] waypoints = new int[waypointStrings.length];
            for (int j = 0; j < waypointStrings.length; j++) {
                waypoints[j] = Integer.parseInt(waypointStrings[j]);
            }

            // Create appropriate monkey type
            if (isNormalMonkey) {
                return (T) new NormalMonkey(x, y, direction, waypoints);
            } else {
                return (T) new IntelligentMonkey(x, y, direction, waypoints);
            }
        }
        return null;
    }
}