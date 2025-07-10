import bagel.*;

/**
 * Represents a bullet projectile fired by Mario when holding a blaster.
 * The bullet travels in a straight horizontal line for a fixed distance.
 */
public class Bullet extends Projectile {
    // Sprite images for left and right directions
    private static final Image BULLET_RIGHT_IMAGE = new Image("res/bullet_right.png");
    private static final Image BULLET_LEFT_IMAGE = new Image("res/bullet_left.png");

    // Movement constants
    private static final double SPEED = 3.8; // Pixels per frame
    private static final double MAX_DISTANCE = 300.0; // Maximum travel distance

    /**
     * Creates a new bullet projectile at the specified position.
     *
     * @param x The initial x-coordinate
     * @param y The initial y-coordinate
     * @param isFacingRight Whether the character that fired the bullet is facing right
     */
    public Bullet(double x, double y, boolean isFacingRight) {
        super(x, y, isFacingRight);
    }

    /**
     * Gets the speed of the bullet.
     *
     * @return The bullet's speed
     */
    @Override
    protected double getSpeed() {
        return SPEED;
    }

    /**
     * Gets the maximum distance the bullet can travel.
     *
     * @return The maximum distance
     */
    @Override
    protected double getMaxDistance() {
        return MAX_DISTANCE;
    }

    /**
     * Gets the appropriate bullet image based on direction.
     * Bullets have different images for left and right directions.
     *
     * @return The bullet image (left or right facing)
     */
    @Override
    protected Image getProjectileImage() {
        return getIsFacingRight() ? BULLET_RIGHT_IMAGE : BULLET_LEFT_IMAGE;
    }
}