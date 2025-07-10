import bagel.*;
import bagel.util.Rectangle;

/**
 * Abstract base class for different monkey types in the game.
 * Provides common properties and behaviors for all monkey variants.
 */
public abstract class Monkey {
    // Common properties
    protected double x;
    protected double y;
    protected double velocity;
    protected boolean isFacingRight;
    protected int[] waypoints;
    protected int currentRouteIndex;
    protected double distanceTravelled;
    protected double width;
    protected double height;
    protected boolean isDestroyed;

    // Constants
    protected static final double MONKEY_SPEED = 0.5;

    /**
     * Creates a new monkey at the specified position.
     *
     * @param x The initial x-coordinate
     * @param y The initial y-coordinate
     * @param direction Initial facing direction ("left" or "right")
     * @param waypoints Array of waypoint distances for monkey movement
     */
    public Monkey(double x, double y, String direction, int[] waypoints) {
        this.x = x;
        this.y = y;
        this.isFacingRight = "right".equalsIgnoreCase(direction);
        this.velocity = isFacingRight ? MONKEY_SPEED : -MONKEY_SPEED;
        this.waypoints = waypoints;
        this.currentRouteIndex = 0;
        this.distanceTravelled = 0;
        this.isDestroyed = false;
    }

    /**
     * Updates the monkey's position and state.
     *
     * @param platforms The platforms to check for collisions
     */
    public abstract void update(Platform[] platforms);

    /**
     * Draws the monkey on screen.
     */
    public abstract void draw();

    /**
     * Marks the monkey as destroyed.
     */
    public void destroy() {
        this.isDestroyed = true;
    }

    /**
     * Checks if the monkey is destroyed.
     *
     * @return true if the monkey is destroyed, false otherwise
     */
    public boolean isDestroyed() {
        return isDestroyed;
    }

    /**
     * Checks if the monkey is at the edge of a platform.
     *
     * @param platforms The platforms to check
     * @return true if the monkey is at an edge, false otherwise
     */
    public boolean isAtEdge(Platform[] platforms) {
        // Check if the monkey is at the edge of any platform
        for (Platform platform : platforms) {
            if (platform == null) continue;

            // Get platform bounds
            double platformLeft = platform.getX() - platform.getWidth() / 2;
            double platformRight = platform.getX() + platform.getWidth() / 2;

            // Check if monkey is on this platform and at its edge
            if (Math.abs(this.y - platform.getY() + this.height / 2) < 5) {
                if ((isFacingRight && Math.abs(this.x + this.width / 2 - platformRight) < 5) ||
                        (!isFacingRight && Math.abs(this.x - this.width / 2 - platformLeft) < 5)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Changes the monkey's sprite based on direction.
     */
    public abstract void changeSprite();

    /**
     * Gets the bounding box of the monkey for collision detection.
     *
     * @return A Rectangle representing the monkey's bounding box
     */
    public Rectangle getBoundingBox() {
        return new Rectangle(x - width / 2, y - height / 2, width, height);
    }

    // Getters for position and dimensions
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}