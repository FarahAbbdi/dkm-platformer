import bagel.*;
import java.util.Properties;

/**
 * The main class for the Shadow Donkey Kong game.
 * This class extends {@code AbstractGame} and is responsible for managing game initialization,
 * updates, rendering, and handling user input.
 *
 * It sets up the game world, initializes characters, platforms, ladders, and other game objects,
 * and runs the game loop to ensure smooth gameplay.
 */
public class ShadowDonkeyKong extends AbstractGame {

    private final Properties GAME_PROPS;
    private final Properties MESSAGE_PROPS;

    private HomeScreen homeScreen;
    private GamePlayScreen gamePlayScreen;
    private GameEndScreen gameEndScreen;

    public static double screenWidth;
    public static double screenHeight;

    /**
     * Constructs a new instance of the ShadowDonkeyKong game.
     * Initializes the game window using provided properties and sets up the home screen.
     *
     * @param gameProps     A {@link Properties} object containing game configuration settings
     *                      such as window width and height.
     * @param messageProps  A {@link Properties} object containing localized messages or UI labels,
     *                      including the title for the home screen.
     */
    public ShadowDonkeyKong(Properties gameProps, Properties messageProps) {
        super(Integer.parseInt(gameProps.getProperty("window.width")),
                Integer.parseInt(gameProps.getProperty("window.height")),
                messageProps.getProperty("home.title"));

        this.GAME_PROPS = gameProps;
        this.MESSAGE_PROPS = messageProps;
        screenWidth = Integer.parseInt(gameProps.getProperty("window.width"));
        screenHeight = Integer.parseInt(gameProps.getProperty("window.height"));

        homeScreen = new HomeScreen(GAME_PROPS, MESSAGE_PROPS);
    }

    /**
     * Render the relevant screen based on the keyboard input given by the user and the status of the gameplay.
     * @param input The current mouse/keyboard input.
     */
    @Override
    protected void update(Input input) {
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        // Home Screen
        if (gamePlayScreen == null && gameEndScreen == null) {
            if (homeScreen.update(input)) {
                // Check which level was selected
                if (homeScreen.getSelectedLevel() == 2) {
                    // Start Level 2
                    gamePlayScreen = new Level2Screen(GAME_PROPS, MESSAGE_PROPS);
                } else {
                    // Start Level 1 (default)
                    gamePlayScreen = new Level1Screen(GAME_PROPS, MESSAGE_PROPS);
                }
            }
        }
        // Gameplay Screen
        else if (gamePlayScreen != null && gameEndScreen == null) {
            // Handle level-specific logic using unified method
            handleLevelLogic(gamePlayScreen, input);

            // Common gameplay update logic
            if (gamePlayScreen != null && gamePlayScreen.update(input)) {
                handleGameEnd();
            }
        }
        // Game Over / Victory Screen
        else if (gamePlayScreen == null) {
            handleEndScreen(input);
        }
    }

    /**
     * Handles level-specific logic and transitions for both Level 1 and Level 2.
     */
    private void handleLevelLogic(GamePlayScreen level, Input input) {
        // Cast to specific level types to access isGameOver() method
        boolean isCompleted = level.isLevelCompleted();
        boolean isGameOver = false;

        // Check game over status based on level type
        if (level instanceof Level1Screen) {
            isGameOver = ((Level1Screen) level).isGameOver();
        } else if (level instanceof Level2Screen) {
            isGameOver = ((Level2Screen) level).isGameOver();
        }

        // If level is completed and SPACE is pressed, go to end screen
        if (isCompleted && input.wasPressed(Keys.SPACE)) {
            createEndScreen(true, level.getScore(), level.getSecondsLeft());
            gamePlayScreen = null;
            return;
        }

        // If game over and SPACE is pressed, go to end screen
        if (isGameOver && input.wasPressed(Keys.SPACE)) {
            createEndScreen(false, level.getScore(), level.getSecondsLeft());
            gamePlayScreen = null;
            return;
        }
    }

    /**
     * Handles the natural end of gameplay (time up, etc.).
     */
    private void handleGameEnd() {
        boolean isWon = gamePlayScreen.isLevelCompleted();
        int finalScore = gamePlayScreen.getScore();
        int timeRemaining = gamePlayScreen.getSecondsLeft();

        createEndScreen(isWon, finalScore, timeRemaining);
        gamePlayScreen = null;
    }

    /**
     * Creates and configures the end screen.
     */
    private void createEndScreen(boolean isWon, int finalScore, int timeRemaining) {
        gameEndScreen = new GameEndScreen(GAME_PROPS, MESSAGE_PROPS);
        gameEndScreen.setIsWon(isWon);
        gameEndScreen.setFinalScore(timeRemaining, finalScore);
    }

    /**
     * Handles end screen logic and transitions.
     */
    private void handleEndScreen(Input input) {
        // If SPACE is pressed, go back to home screen
        if (input.wasPressed(Keys.SPACE)) {
            resetToHomeScreen();
            return;
        }
        // Default endScreen update behavior
        else if (gameEndScreen.update(input)) {
            resetToHomeScreen();
        }
    }

    /**
     * Resets the game to the home screen.
     */
    private void resetToHomeScreen() {
        gamePlayScreen = null;
        gameEndScreen = null;
        // Optionally reset the home screen
        homeScreen = new HomeScreen(GAME_PROPS, MESSAGE_PROPS);
    }

    /**
     * Retrieves the width of the game screen.
     *
     * @return The width of the screen in pixels.
     */
    public static double getScreenWidth() {
        return screenWidth;
    }

    /**
     * Retrieves the height of the game screen.
     *
     * @return The height of the screen in pixels.
     */
    public static double getScreenHeight() {
        return screenHeight;
    }

    /**
     * The main entry point of the Shadow Donkey Kong game.
     *
     * This method loads the game properties and message files, initializes the game,
     * and starts the game loop.
     *
     * @param args Command-line arguments (not used in this game).
     */
    public static void main(String[] args) {
        Properties gameProps = IOUtils.readPropertiesFile("res/app.properties");
        Properties messageProps = IOUtils.readPropertiesFile("res/message_en.properties");
        ShadowDonkeyKong game = new ShadowDonkeyKong(gameProps, messageProps);
        game.run();
    }
}