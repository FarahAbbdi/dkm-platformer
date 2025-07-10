import bagel.*;

/**
 * Handles Mario's sprite management and rendering.
 */
public class MarioSpriteManager {
    // Mario images for different states
    private final Image MARIO_RIGHT_IMAGE;
    private final Image MARIO_LEFT_IMAGE;
    private final Image MARIO_HAMMER_LEFT_IMAGE;
    private final Image MARIO_HAMMER_RIGHT_IMAGE;
    private final Image MARIO_BLASTER_RIGHT_IMAGE;
    private final Image MARIO_BLASTER_LEFT_IMAGE;

    private Image currentImage;
    private final Mario mario;

    public MarioSpriteManager(Mario mario) {
        this.mario = mario;

        // Load all Mario sprites
        this.MARIO_RIGHT_IMAGE = new Image("res/mario_right.png");
        this.MARIO_LEFT_IMAGE = new Image("res/mario_left.png");
        this.MARIO_HAMMER_RIGHT_IMAGE = new Image("res/mario_hammer_right.png");
        this.MARIO_HAMMER_LEFT_IMAGE = new Image("res/mario_hammer_left.png");
        this.MARIO_BLASTER_RIGHT_IMAGE = new Image("res/mario_blaster_right.png");
        this.MARIO_BLASTER_LEFT_IMAGE = new Image("res/mario_blaster_left.png");

        // Default to hammer right image
        this.currentImage = MARIO_HAMMER_RIGHT_IMAGE;

        // Set initial dimensions
        mario.setWidth(currentImage.getWidth());
        mario.setHeight(currentImage.getHeight());
    }

    /**
     * Updates Mario's sprite based on his current state.
     */
    public void updateSprite() {
        // Remember old image for height adjustment
        Image oldImage = currentImage;
        double oldHeight = oldImage.getHeight();
        double oldBottom = mario.getY() + (oldHeight / 2);

        // Select new image based on state and direction
        if (mario.hasHammer()) {
            currentImage = mario.isFacingRight() ? MARIO_HAMMER_RIGHT_IMAGE : MARIO_HAMMER_LEFT_IMAGE;
        } else if (mario.hasBlaster()) {
            currentImage = mario.isFacingRight() ? MARIO_BLASTER_RIGHT_IMAGE : MARIO_BLASTER_LEFT_IMAGE;
        } else {
            currentImage = mario.isFacingRight() ? MARIO_RIGHT_IMAGE : MARIO_LEFT_IMAGE;
        }

        // Adjust Mario's position to keep bottom edge consistent
        double newHeight = currentImage.getHeight();
        double newBottom = mario.getY() + (newHeight / 2);

        // Shift Y position to maintain bottom edge alignment
        mario.setY(mario.getY() - (newBottom - oldBottom));

        // Update Mario's dimensions
        mario.setWidth(currentImage.getWidth());
        mario.setHeight(newHeight);
    }

    /**
     * Draws Mario on the screen.
     */
    public void draw() {
        currentImage.draw(mario.getX(), mario.getY());
    }

    /**
     * Gets the current image dimensions.
     */
    public double getWidth() {
        return currentImage.getWidth();
    }

    public double getHeight() {
        return currentImage.getHeight();
    }
}