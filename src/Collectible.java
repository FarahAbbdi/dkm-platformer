import bagel.*;
import bagel.util.Rectangle;

/**
 * Abstract base class for collectible items that can be picked up by Mario.
 * Provides common functionality for positioning, collection state, and collision detection.
 */
public abstract class Collectible {
    // Position - private to enforce encapsulation
    private final double x;
    private final double y;

    // State - private to enforce encapsulation
    private boolean isCollected = false;

    // Abstract methods to be implemented by subclasses
    protected abstract Image getCollectibleImage();
    protected abstract String getCollectibleName();

    /**
     * Creates a new collectible at the specified position.
     *
     * @param x The x-coordinate
     * @param y The y-coordinate
     */
    public Collectible(double x, double y) {
        this.x = x;
        this.y = y;
        this.isCollected = false;
    }

    /**
     * Draws the collectible on screen if it has not been collected.
     */
    public void draw() {
        if (!isCollected()) {
            getCollectibleImage().draw(getX(), getY());
        }
    }

    /**
     * Marks the collectible as collected.
     */
    public void collect() {
        System.out.println(getCollectibleName() + " at (" + getX() + "," + getY() + ") collected!");
        setCollected(true);
    }

    /**
     * Checks if the collectible has been collected.
     *
     * @return true if the collectible has been collected, false otherwise
     */
    public boolean isCollected() {
        return isCollected;
    }

    /**
     * Sets the collection state of the collectible.
     *
     * @param collected true if collected, false otherwise
     */
    protected void setCollected(boolean collected) {
        this.isCollected = collected;
    }

    /**
     * Checks if the collectible is active (not collected).
     *
     * @return true if the collectible is active, false otherwise
     */
    public boolean isActive() {
        return !isCollected();
    }

    /**
     * Gets the bounding box of the collectible for collision detection.
     *
     * @return A Rectangle representing the collectible's bounding box
     */
    public Rectangle getBoundingBox() {
        if (isCollected()) {
            return new Rectangle(-1000, -1000, 0, 0); // Move off-screen if collected
        }

        Image image = getCollectibleImage();
        return new Rectangle(
                getX() - image.getWidth() / 2,
                getY() - image.getHeight() / 2,
                image.getWidth(),
                image.getHeight()
        );
    }

    /**
     * Gets the x-coordinate of the collectible.
     *
     * @return The x-coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the collectible.
     *
     * @return The y-coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Gets the width of the collectible.
     *
     * @return The width
     */
    public double getWidth() {
        return getCollectibleImage().getWidth();
    }

    /**
     * Gets the height of the collectible.
     *
     * @return The height
     */
    public double getHeight() {
        return getCollectibleImage().getHeight();
    }
}