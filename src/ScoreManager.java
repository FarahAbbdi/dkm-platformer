/**
 * Manages game scoring and related calculations.
 * Decouples score management from the main game logic.
 */
public class ScoreManager {
    private int score = 0;

    /**
     * Adds points to the current score.
     *
     * @param points Points to add
     */
    public void addScore(int points) {
        score += points;
        System.out.println("Score added: " + points + ", Total: " + score);
    }

    /**
     * Gets the current score.
     *
     * @return Current score
     */
    public int getScore() {
        return score;
    }

    /**
     * Resets the score to zero.
     */
    public void resetScore() {
        score = 0;
    }

    /**
     * Sets the score to a specific value.
     *
     * @param newScore The new score value
     */
    public void setScore(int newScore) {
        score = newScore;
    }
}