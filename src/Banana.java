import bagel.*;

/**
 * Represents a banana projectile thrown by an intelligent monkey.
 * The banana travels in a straight horizontal line for a fixed distance.
 */
public class Banana extends Projectile {
    // Sprite image
    private static final Image BANANA_IMAGE = new Image("res/banana.png");

    // Movement constants
    private static final double SPEED = 1.8; // Exactly 1.8 pixels per frame
    private static final double MAX_DISTANCE = 300.0; // Travel 300 pixels before stopping

    /**
     * Creates a new banana projectile at the specified position.
     *
     * @param x The initial x-coordinate
     * @param y The initial y-coordinate
     * @param isFacingRight Whether the monkey that threw the banana is facing right
     */
    public Banana(double x, double y, boolean isFacingRight) {
        super(x, y, isFacingRight);
    }

    /**
     * Gets the speed of the banana.
     *
     * @return The banana's speed
     */
    @Override
    protected double getSpeed() {
        return SPEED;
    }

    /**
     * Gets the maximum distance the banana can travel.
     *
     * @return The maximum distance
     */
    @Override
    protected double getMaxDistance() {
        return MAX_DISTANCE;
    }

    /**
     * Gets the banana image.
     *
     * @return The banana image
     */
    @Override
    protected Image getProjectileImage() {
        return BANANA_IMAGE;
    }
}