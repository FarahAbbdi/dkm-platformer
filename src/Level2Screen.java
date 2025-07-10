import bagel.*;
import java.util.Properties;

/**
 * Level 2 implementation using unified managers.
 * Handles the second level with advanced mechanics including monkeys, bananas, and bullets.
 */
public class Level2Screen extends GamePlayScreen {

    // Managers
    private final GameObjectManager gameObjectManager;
    private final CollisionManager collisionManager;
    private final ScoreManager scoreManager;
    private final GameObjectFactory gameObjectFactory;

    // Game state
    private final Image background;
    private int currFrame = 0;
    private final int MAX_FRAMES;
    private boolean isGameOver = false;

    // Display components
    private final Font STATUS_FONT;
    private final int SCORE_X;
    private final int SCORE_Y;
    private final int DONKEY_HEALTH_X;
    private final int DONKEY_HEALTH_Y;
    private static final int BULLET_DISPLAY_DIFF_Y = 30;
    private static final int TIME_DISPLAY_DIFF_Y = 30;
    private static final String SCORE_MESSAGE = "SCORE ";
    private static final String TIME_MESSAGE = "Time Left ";
    private static final String HEALTH_MESSAGE = "Donkey Health ";
    private static final String BULLETS_MESSAGE = "Bullets left ";
    private static final int INITIAL_DONKEY_HEALTH = 5;

    private int donkeyHealth = INITIAL_DONKEY_HEALTH;

    /**
     * Constructs a new Level 2 screen with managers.
     *
     * @param gameProps Properties containing game configuration
     * @param msgProps Properties containing message strings
     */
    public Level2Screen(Properties gameProps, Properties msgProps) {
        super(gameProps, msgProps);

        // Initialize managers for Level 2
        this.scoreManager = new ScoreManager();
        this.gameObjectManager = new GameObjectManager(2); // Level 2
        this.gameObjectFactory = new GameObjectFactory(gameProps, 2);
        this.collisionManager = new CollisionManager(gameObjectManager, scoreManager);

        // Load display properties
        this.MAX_FRAMES = Integer.parseInt(gameProps.getProperty("gamePlay.maxFrames"));
        this.STATUS_FONT = new Font(
                gameProps.getProperty("font"),
                Integer.parseInt(gameProps.getProperty("gamePlay.score.fontSize"))
        );
        this.SCORE_X = Integer.parseInt(gameProps.getProperty("gamePlay.score.x"));
        this.SCORE_Y = Integer.parseInt(gameProps.getProperty("gamePlay.score.y"));
        String[] healthCoords = gameProps.getProperty("gamePlay.donkeyhealth.coords").split(",");
        this.DONKEY_HEALTH_X = Integer.parseInt(healthCoords[0].trim());
        this.DONKEY_HEALTH_Y = Integer.parseInt(healthCoords[1].trim());
        this.background = new Image(gameProps.getProperty("backgroundImage"));

        // Initialize game objects
        initializeGameObjects();
    }

    /**
     * Initializes Level 2 game objects using the factory.
     */
    @Override
    protected void initializeGameObjects() {
        // Create common objects for Level 2
        gameObjectManager.setMario(gameObjectFactory.createMario());
        gameObjectManager.setDonkey(gameObjectFactory.createDonkey());
        gameObjectManager.setBarrels(gameObjectFactory.createBarrels());
        gameObjectManager.setLadders(gameObjectFactory.createLadders());
        gameObjectManager.setPlatforms(gameObjectFactory.createPlatforms());
        gameObjectManager.setHammers(gameObjectFactory.createHammers());

        // Create Level 2 specific objects
        gameObjectManager.setBlasters(gameObjectFactory.createBlasters());
        gameObjectManager.setNormalMonkeys(gameObjectFactory.createNormalMonkeys());
        gameObjectManager.setIntelligentMonkeys(gameObjectFactory.createIntelligentMonkeys());
    }

    /**
     * Updates Level 2 game state each frame.
     *
     * @param input The current player input.
     * @return {@code true} if the game ends, {@code false} otherwise.
     */
    @Override
    public boolean update(Input input) {
        currFrame++;

        // Draw background
        background.drawFromTopLeft(0, 0);

        // Update all game objects using manager
        gameObjectManager.updateGameObjects(input);

        // Update barrels with collision checks
        updateBarrels();

        // Check game time
        if (checkingGameTime()) {
            isGameOver = true;
        }

        // Check all collisions using manager
        CollisionManager.GameCollisionResult collisionResult = collisionManager.checkAllCollisions();

        // Update game state based on collision results
        if (collisionResult.isGameOver()) {
            isGameOver = true;
        }

        // Update donkey health for display (Level 2 specific)
        donkeyHealth = gameObjectManager.getDonkey().getHealth();

        // Display game information
        displayInfo();

        return isGameOver || isLevelCompleted();
    }

    /**
     * Updates barrels with their physics and drawing.
     */
    private void updateBarrels() {
        for (Barrel barrel : gameObjectManager.getBarrels()) {
            if (barrel != null) {
                barrel.update(gameObjectManager.getPlatforms());
                barrel.draw();
            }
        }
    }

    /**
     * Displays the player's score, time left, donkey health, and bullet count on screen.
     */
    @Override
    public void displayInfo() {
        STATUS_FONT.drawString(SCORE_MESSAGE + scoreManager.getScore(), SCORE_X, SCORE_Y);

        // Time left in seconds
        int secondsLeft = getSecondsLeft();
        int TIME_Y = SCORE_Y + TIME_DISPLAY_DIFF_Y;
        STATUS_FONT.drawString(TIME_MESSAGE + secondsLeft, SCORE_X, TIME_Y);

        // Display donkey health
        STATUS_FONT.drawString(HEALTH_MESSAGE + donkeyHealth, DONKEY_HEALTH_X, DONKEY_HEALTH_Y);

        // Display bullets count 30 pixels below Donkey health
        int BULLETS_Y = DONKEY_HEALTH_Y + BULLET_DISPLAY_DIFF_Y;
        int[] bulletCounter = gameObjectManager.getBulletCounter();
        STATUS_FONT.drawString(BULLETS_MESSAGE + bulletCounter[0], DONKEY_HEALTH_X, BULLETS_Y);
    }

    /**
     * Checks whether Level 1 is completed.
     * Level 1 is completed when Mario reaches Donkey while holding a hammer.
     *
     * @return {@code true} if the level is completed, {@code false} otherwise.
     */
    @Override
    public boolean isLevelCompleted() {
        Mario mario = gameObjectManager.getMario();
        Donkey donkey = gameObjectManager.getDonkey();
        return mario.hasReached(donkey) && mario.holdHammer() || donkey.isDefeated();
    }

    /**
     * Checks if the game has reached its time limit.
     *
     * @return {@code true} if the time limit has been reached, {@code false} otherwise.
     */
    public boolean checkingGameTime() {
        return currFrame >= MAX_FRAMES;
    }

    /**
     * Gets the current score.
     *
     * @return The current score
     */
    @Override
    public int getScore() {
        return scoreManager.getScore();
    }

    /**
     * Gets the number of seconds left in the level.
     *
     * @return The number of seconds left
     */
    @Override
    public int getSecondsLeft() {
        return (MAX_FRAMES - currFrame) / 60;
    }

    /**
     * Checks if the game is over.
     *
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() {
        return isGameOver;
    }
}