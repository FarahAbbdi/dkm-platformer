import bagel.*;
import java.util.Properties;

/**
 * Level 1 implementation using unified managers.
 * Handles the first level of the game with basic mechanics.
 */
public class Level1Screen extends GamePlayScreen {

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
    private static final int TIME_DISPLAY_DIFF_Y = 30;
    private static final String SCORE_MESSAGE = "SCORE ";
    private static final String TIME_MESSAGE = "Time Left ";
    private static final String HEALTH_MESSAGE = "Donkey Health ";
    private static final int INITIAL_DONKEY_HEALTH = 100;

    /**
     * Constructs a new Level 1 screen with managers.
     *
     * @param gameProps Properties containing game configuration
     * @param msgProps Properties containing message strings
     */
    public Level1Screen(Properties gameProps, Properties msgProps) {
        super(gameProps, msgProps);

        // Initialize managers for Level 1
        this.scoreManager = new ScoreManager();
        this.gameObjectManager = new GameObjectManager(1); // Level 1
        this.gameObjectFactory = new GameObjectFactory(gameProps, 1);
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
     * Initializes Level 1 game objects using the factory.
     */
    @Override
    protected void initializeGameObjects() {
        // Create Level 1 objects using factory
        gameObjectManager.setMario(gameObjectFactory.createMario());
        gameObjectManager.setDonkey(gameObjectFactory.createDonkey());
        gameObjectManager.setBarrels(gameObjectFactory.createBarrels());
        gameObjectManager.setLadders(gameObjectFactory.createLadders());
        gameObjectManager.setPlatforms(gameObjectFactory.createPlatforms());
        gameObjectManager.setHammers(gameObjectFactory.createHammers());

        // Level 1 doesn't have blasters, monkeys, or bananas - they remain null
    }

    /**
     * Updates Level 1 game state each frame.
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
     * Displays the player's score, time left, and donkey health on screen.
     */
    @Override
    public void displayInfo() {
        STATUS_FONT.drawString(SCORE_MESSAGE + scoreManager.getScore(), SCORE_X, SCORE_Y);

        // Time left in seconds
        int secondsLeft = getSecondsLeft();
        int TIME_Y = SCORE_Y + TIME_DISPLAY_DIFF_Y;
        STATUS_FONT.drawString(TIME_MESSAGE + secondsLeft, SCORE_X, TIME_Y);

        // Display donkey health (static for Level 1)
        STATUS_FONT.drawString(HEALTH_MESSAGE + INITIAL_DONKEY_HEALTH, DONKEY_HEALTH_X, DONKEY_HEALTH_Y);
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
        return mario.hasReached(donkey) && mario.holdHammer();
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