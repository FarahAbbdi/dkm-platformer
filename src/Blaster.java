import bagel.*;

/**
 * Represents a blaster that can be collected by Mario.
 * The blaster allows Mario to shoot bullets when collected.
 */
public class Blaster extends Collectible {
    // Sprite image
    private static final Image BLASTER_IMAGE = new Image("res/blaster.png");

    /**
     * Creates a new blaster at the specified position.
     *
     * @param x The x-coordinate
     * @param y The y-coordinate
     */
    public Blaster(double x, double y) {
        super(x, y);
    }

    /**
     * Gets the blaster image.
     *
     * @return The blaster image
     */
    @Override
    protected Image getCollectibleImage() {
        return BLASTER_IMAGE;
    }

    /**
     * Gets the name of this collectible for debug output.
     *
     * @return The collectible name
     */
    @Override
    protected String getCollectibleName() {
        return "Blaster";
    }
}