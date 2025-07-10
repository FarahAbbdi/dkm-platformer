import bagel.Input;
import java.util.ArrayList;

/**
 * Game object manager that handles both Level 1 and Level 2.
 * Uses composition to handle level-specific objects.
 */
public class GameObjectManager {
    // Core game objects (present in both levels)
    private Mario mario;
    private Donkey donkey;
    private Barrel[] barrels;
    private Ladder[] ladders;
    private Hammer[] hammers;
    private Platform[] platforms;

    // Level 2 specific objects (null in Level 1)
    private NormalMonkey[] normalMonkeys;
    private IntelligentMonkey[] intelligentMonkeys;
    private Blaster[] blasters;
    private ArrayList<Banana> bananas;
    private final int[] bulletCounter = new int[1];

    // Level identifier
    private final int levelNumber;

    /**
     * Creates a new GameObjectManager for the specified level.
     *
     * @param levelNumber The level number (1 or 2)
     */
    public GameObjectManager(int levelNumber) {
        this.levelNumber = levelNumber;
        if (levelNumber == 2) {
            this.bananas = new ArrayList<>();
        }
    }

    // Setters for initialization
    public void setMario(Mario mario) { this.mario = mario; }
    public void setDonkey(Donkey donkey) { this.donkey = donkey; }
    public void setBarrels(Barrel[] barrels) { this.barrels = barrels; }
    public void setLadders(Ladder[] ladders) { this.ladders = ladders; }
    public void setHammers(Hammer[] hammers) { this.hammers = hammers; }
    public void setPlatforms(Platform[] platforms) { this.platforms = platforms; }

    // Level 2 specific setters
    public void setNormalMonkeys(NormalMonkey[] normalMonkeys) {
        this.normalMonkeys = normalMonkeys;
    }
    public void setIntelligentMonkeys(IntelligentMonkey[] intelligentMonkeys) {
        this.intelligentMonkeys = intelligentMonkeys;
    }
    public void setBlasters(Blaster[] blasters) { this.blasters = blasters; }

    // Getters
    public Mario getMario() { return mario; }
    public Donkey getDonkey() { return donkey; }
    public Barrel[] getBarrels() { return barrels; }
    public Ladder[] getLadders() { return ladders; }
    public Hammer[] getHammers() { return hammers; }
    public Platform[] getPlatforms() { return platforms; }
    public NormalMonkey[] getNormalMonkeys() { return normalMonkeys; }
    public IntelligentMonkey[] getIntelligentMonkeys() { return intelligentMonkeys; }
    public Blaster[] getBlasters() { return blasters; }
    public ArrayList<Banana> getBananas() { return bananas; }
    public int[] getBulletCounter() { return bulletCounter; }
    public int getLevelNumber() { return levelNumber; }

    /**
     * Updates all game objects based on the current level.
     */
    public void updateGameObjects(Input input) {
        // Common updates for both levels
        updateCommonObjects(input);

        // Level-specific updates
        if (levelNumber == 2) {
            updateLevel2SpecificObjects();
        }
    }

    /**
     * Updates objects common to both levels.
     */
    private void updateCommonObjects(Input input) {
        // Update platforms
        for (Platform platform : platforms) {
            if (platform != null) {
                platform.draw();
            }
        }

        // Update ladders
        for (Ladder ladder : ladders) {
            if (ladder != null) {
                ladder.update(platforms);
                ladder.draw();
            }
        }

        // Update Mario (different method calls based on level)
        if (levelNumber == 1) {
            mario.update(input, ladders, platforms, hammers);
        } else if (levelNumber == 2) {
            mario.update(input, ladders, platforms, hammers, blasters, bulletCounter);
            updateAndDrawBullets();
        }

        // Update Donkey
        donkey.update(platforms);
        donkey.draw();

        // Draw hammers
        drawHammers();
    }

    /**
     * Updates Level 2 specific objects.
     */
    private void updateLevel2SpecificObjects() {
        // Draw blasters
        if (blasters != null) {
            for (Blaster blaster : blasters) {
                if (blaster != null) {
                    blaster.draw();
                }
            }
        }

        // Update monkeys
        updateMonkeys();

        // Update bananas
        updateBananas();

        // Handle weapon switching
        handleWeaponSwitching();
    }

    /**
     * Updates and draws bullets (Level 2 only).
     */
    private void updateAndDrawBullets() {
        if (mario.getBullets() != null && !mario.getBullets().isEmpty()) {
            java.util.Iterator<Bullet> bulletIterator = mario.getBullets().iterator();
            while (bulletIterator.hasNext()) {
                Bullet bullet = bulletIterator.next();

                boolean shouldRemove = bullet.update();
                if (shouldRemove) {
                    bulletIterator.remove();
                    continue;
                }

                bullet.draw();
            }
        }
    }

    /**
     * Updates monkeys (Level 2 only).
     */
    private void updateMonkeys() {
        // Update normal monkeys
        if (normalMonkeys != null) {
            for (NormalMonkey monkey : normalMonkeys) {
                if (monkey != null && !monkey.isDestroyed()) {
                    monkey.update(platforms);
                    monkey.draw();
                }
            }
        }

        // Update intelligent monkeys
        if (intelligentMonkeys != null) {
            for (IntelligentMonkey monkey : intelligentMonkeys) {
                if (monkey != null && !monkey.isDestroyed()) {
                    monkey.update(platforms);
                    monkey.draw();

                    // Check if monkey throws a banana
                    Banana newBanana = monkey.checkThrowBanana();
                    if (newBanana != null) {
                        bananas.add(newBanana);
                    }
                }
            }
        }
    }

    /**
     * Updates bananas (Level 2 only).
     */
    private void updateBananas() {
        if (bananas != null) {
            java.util.Iterator<Banana> bananaIterator = bananas.iterator();
            while (bananaIterator.hasNext()) {
                Banana banana = bananaIterator.next();
                boolean shouldRemove = banana.update();

                if (!shouldRemove) {
                    banana.draw();
                }

                if (shouldRemove) {
                    bananaIterator.remove();
                }
            }
        }
    }

    /**
     * Draws hammers.
     */
    private void drawHammers() {
        for (Hammer hammer : hammers) {
            if (hammer != null) {
                hammer.draw();
            }
        }
    }

    /**
     * Handles weapon switching (Level 2 only).
     */
    private void handleWeaponSwitching() {
        if (levelNumber == 2 && mario.holdHammer()) {
            bulletCounter[0] = 0;
        }
    }

    /**
     * Checks if this manager is handling Level 2.
     */
    public boolean isLevel2() {
        return levelNumber == 2;
    }
}