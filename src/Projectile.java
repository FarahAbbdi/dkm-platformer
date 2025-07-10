import bagel.*;
import bagel.util.Rectangle;

/**
 * Abstract base class for projectiles that travel horizontally.
 * Provides common functionality for movement, collision detection, and lifecycle management.
 */
public abstract class Projectile {
    // Position and movement - private to enforce encapsulation
    private double x;
    private double y;
    private double velocityX;
    private double distanceTravelled;
    private boolean isFacingRight;

    // State - private to enforce encapsulation
    private boolean isDestroyed;

    // Movement constants (to be overridden by subclasses)
    protected abstract double getSpeed();
    protected abstract double getMaxDistance();
    protected abstract Image getProjectileImage();

    /**
     * Creates a new projectile at the specified position.
     *
     * @param x The initial x-coordinate
     * @param y The initial y-coordinate
     * @param isFacingRight Whether the character that fired the projectile is facing right
     */
    public Projectile(double x, double y, boolean isFacingRight) {
        this.x = x;
        this.y = y;
        this.isFacingRight = isFacingRight;
        this.velocityX = isFacingRight ? getSpeed() : -getSpeed();
        this.distanceTravelled = 0;
        this.isDestroyed = false;
    }

    // Protected getters and setters for subclasses to access private fields

    /**
     * Gets the x-coordinate of the projectile.
     *
     * @return The x-coordinate
     */
    protected double getProjectileX() {
        return x;
    }

    /**
     * Sets the x-coordinate of the projectile.
     *
     * @param x The new x-coordinate
     */
    protected void setProjectileX(double x) {
        this.x = x;
    }

    /**
     * Gets the y-coordinate of the projectile.
     *
     * @return The y-coordinate
     */
    protected double getProjectileY() {
        return y;
    }

    /**
     * Gets the velocity in the x-direction.
     *
     * @return The x-velocity
     */
    protected double getVelocityX() {
        return velocityX;
    }

    /**
     * Gets the distance travelled by the projectile.
     *
     * @return The distance travelled
     */
    protected double getDistanceTravelled() {
        return distanceTravelled;
    }

    /**
     * Sets the distance travelled by the projectile.
     *
     * @param distance The new distance travelled
     */
    protected void setDistanceTravelled(double distance) {
        this.distanceTravelled = distance;
    }

    /**
     * Gets whether the projectile is facing right.
     *
     * @return true if facing right, false if facing left
     */
    protected boolean getIsFacingRight() {
        return isFacingRight;
    }

    /**
     * Sets whether the projectile is destroyed.
     *
     * @param destroyed true if destroyed, false otherwise
     */
    protected void setDestroyed(boolean destroyed) {
        this.isDestroyed = destroyed;
    }

    /**
     * Updates the projectile's position.
     *
     * @return true if the projectile should be removed, false otherwise
     */
    public boolean update() {
        if (isDestroyed()) return true;

        // Move horizontally
        setProjectileX(getProjectileX() + getVelocityX());

        // Track distance traveled
        setDistanceTravelled(getDistanceTravelled() + Math.abs(getVelocityX()));

        // Check if traveled maximum distance
        if (getDistanceTravelled() >= getMaxDistance()) {
            destroy();
            return true;
        }

        // Check if out of bounds of screen
        if (getProjectileX() < 0 || getProjectileX() > Window.getWidth() ||
                getProjectileY() < 0 || getProjectileY() > Window.getHeight()) {
            destroy();
            return true;
        }

        return false;
    }

    /**
     * Draws the projectile on screen.
     */
    public void draw() {
        if (!isDestroyed()) {
            getProjectileImage().draw(getProjectileX(), getProjectileY());
        }
    }

    /**
     * Marks the projectile as destroyed.
     */
    public void destroy() {
        setDestroyed(true);
    }

    /**
     * Checks if the projectile is destroyed.
     *
     * @return true if the projectile is destroyed, false otherwise
     */
    public boolean isDestroyed() {
        return isDestroyed;
    }

    /**
     * Gets the bounding box of the projectile for collision detection.
     *
     * @return A Rectangle representing the projectile's bounding box
     */
    public Rectangle getBoundingBox() {
        Image image = getProjectileImage();
        return new Rectangle(
                getProjectileX() - image.getWidth() / 2,
                getProjectileY() - image.getHeight() / 2,
                image.getWidth(),
                image.getHeight()
        );
    }

    /**
     * Gets the x-coordinate of the projectile (public interface).
     *
     * @return The x-coordinate
     */
    public double getX() {
        return getProjectileX();
    }

    /**
     * Gets the y-coordinate of the projectile (public interface).
     *
     * @return The y-coordinate
     */
    public double getY() {
        return getProjectileY();
    }

    /**
     * Gets the width of the projectile.
     *
     * @return The width
     */
    public double getWidth() {
        return getProjectileImage().getWidth();
    }

    /**
     * Gets the height of the projectile.
     *
     * @return The height
     */
    public double getHeight() {
        return getProjectileImage().getHeight();
    }

    /**
     * Checks if the projectile is facing right (public interface).
     *
     * @return true if facing right, false if facing left
     */
    public boolean isFacingRight() {
        return getIsFacingRight();
    }
}