import bagel.*;
import java.util.Properties;

/**
 * Abstract class representing a game play screen.
 * This serves as the parent class for Level1Screen and future Level2Screen.
 */
public abstract class GamePlayScreen {
    private final Properties gameProps;
    private final Properties msgProps;

    /**
     * Constructs a new GamePlayScreen.
     *
     * @param gameProps Properties containing game configuration
     * @param msgProps Properties containing message strings
     */
    public GamePlayScreen(Properties gameProps, Properties msgProps) {
        this.gameProps = gameProps;
        this.msgProps = msgProps;
    }

    /**
     * Gets the game properties.
     *
     * @return The game properties
     */
    protected Properties getGameProps() {
        return gameProps;
    }

    /**
     * Gets the message properties.
     *
     * @return The message properties
     */
    protected Properties getMsgProps() {
        return msgProps;
    }

    /**
     * Initializes all game objects for this screen.
     */
    protected abstract void initializeGameObjects();

    /**
     * Updates the game state.
     *
     * @param input The current input state
     * @return true if the screen should transition, false otherwise
     */
    public abstract boolean update(Input input);

    /**
     * Displays game information like score and time.
     */
    public abstract void displayInfo();

    /**
     * Checks if the level is completed.
     *
     * @return true if the level is completed, false otherwise
     */
    public abstract boolean isLevelCompleted();

    /**
     * Gets the current score.
     *
     * @return The current score
     */
    public abstract int getScore();

    /**
     * Gets the number of seconds left in the level.
     *
     * @return The number of seconds left
     */
    public abstract int getSecondsLeft();
}