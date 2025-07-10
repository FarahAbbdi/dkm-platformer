import bagel.*;

/**
 * Represents a Hammer collectible in the game.
 * The hammer can be collected by the player to destroy enemies and barrels.
 */
public class Hammer extends Collectible {
    // Sprite image
    private static final Image HAMMER_IMAGE = new Image("res/hammer.png");

    /**
     * Constructs a Hammer at the specified position.
     *
     * @param startX The initial x-coordinate of the hammer.
     * @param startY The initial y-coordinate of the hammer.
     */
    public Hammer(double startX, double startY) {
        super(startX, startY);
    }

    /**
     * Gets the hammer image.
     *
     * @return The hammer image
     */
    @Override
    protected Image getCollectibleImage() {
        return HAMMER_IMAGE;
    }

    /**
     * Gets the name of this collectible for debug output.
     *
     * @return The collectible name
     */
    @Override
    protected String getCollectibleName() {
        return "Hammer";
    }
}