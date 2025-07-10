import bagel.*;
import bagel.util.Rectangle;

/**
 * Represents Donkey Kong in the game, affected by gravity and platform collisions.
 * The Donkey object moves downward due to gravity and lands on platforms when applicable.
 * Donkey now has a health system and can be defeated by bullets.
 */
public class Donkey {
    private final Image DONKEY_IMAGE;
    private final double X; // constant because x does not change, only relying on falling
    private double y;
    private double velocityY = 0;

    // Health system
    private int health = 5;
    private static final int MAX_HEALTH = 5;
    private boolean isDefeated = false;
    private int hitCooldown = 0;
    private static final int HIT_COOLDOWN_FRAMES = 20; // Invulnerability frames after being hit

    /**
     * Constructs a new Donkey at the specified starting position.
     *
     * @param startX The initial x-coordinate of Donkey.
     * @param startY The initial y-coordinate of Donkey.
     */
    public Donkey(double startX, double startY) {
        this.DONKEY_IMAGE = new Image("res/donkey_kong.png"); // Load Donkey Kong sprite
        this.X = startX;
        this.y = startY;
    }

    /**
     * Updates Donkey's position by applying gravity and checking for platform collisions.
     * If Donkey lands on a platform, the velocity is reset to zero.
     * Also updates hit cooldown if active.
     *
     * @param platforms An array of platforms Donkey can land on.
     */
    public void update(Platform[] platforms) {
        // Update hit cooldown if active
        if (hitCooldown > 0) {
            hitCooldown--;
        }

        // If defeated, don't apply physics
        if (isDefeated) {
            draw(); // Still draw Donkey, even if defeated
            return;
        }

        // Apply gravity
        velocityY += Physics.DONKEY_GRAVITY;
        y += velocityY;
        if (velocityY > Physics.DONKEY_TERMINAL_VELOCITY) {
            velocityY = Physics.DONKEY_TERMINAL_VELOCITY;
        }

        // Check for platform collisions
        for (Platform platform : platforms) {
            if (isTouchingPlatform(platform)) {
                // Position Donkey on top of the platform
                y = platform.getY() - (platform.getHeight() / 2) - (DONKEY_IMAGE.getHeight() / 2);
                velocityY = 0; // Stop downward movement
                break;
            }
        }

        // Draw Donkey
        draw();
    }

    /**
     * Checks if Donkey is colliding with a given platform.
     *
     * @param platform The platform to check for collision.
     * @return {@code true} if Donkey is touching the platform, {@code false} otherwise.
     */
    private boolean isTouchingPlatform(Platform platform) {
        Rectangle donkeyBounds = getBoundingBox();
        return donkeyBounds.intersects(platform.getBoundingBox());
    }

    /**
     * Draws Donkey on the screen with visual feedback for damage.
     */
    public void draw() {
        // Draw Donkey with flashing effect when hit
        if (!isDefeated && (hitCooldown <= 0 || hitCooldown % 4 >= 2)) {
            // Flash every few frames during cooldown
            DONKEY_IMAGE.draw(X, y);
        } else if (isDefeated) {
            // If defeated, draw with transparency to indicate defeat
            DrawOptions options = new DrawOptions();
            DONKEY_IMAGE.draw(X, y, options);
        }
        // Not visible during part of the hit cooldown (flashing effect)
    }

    /**
     * Returns Donkey's bounding box for collision detection.
     *
     * @return A {@link Rectangle} representing Donkey's bounding box.
     */
    public Rectangle getBoundingBox() {
        return new Rectangle(
                X - (DONKEY_IMAGE.getWidth() / 2),
                y - (DONKEY_IMAGE.getHeight() / 2),
                DONKEY_IMAGE.getWidth(),
                DONKEY_IMAGE.getHeight()
        );
    }

    /**
     * Gets the current health of Donkey.
     *
     * @return The current health points
     */
    public int getHealth() {
        return health;
    }

    /**
     * Checks if Donkey has been defeated (health reduced to zero).
     *
     * @return true if defeated, false otherwise
     */
    public boolean isDefeated() {
        return isDefeated;
    }

    /**
     * Damages Donkey when hit by a bullet.
     *
     * @return true if this hit defeated Donkey, false otherwise
     */
    public boolean takeDamage() {
        // Only take damage if not in cooldown period and not already defeated
        if (hitCooldown <= 0 && !isDefeated) {
            health--;
            hitCooldown = HIT_COOLDOWN_FRAMES;

            System.out.println("Donkey hit! Health: " + health);

            // Check if defeated
            if (health <= 0) {
                health = 0;
                isDefeated = true;
                return true;
            }
        }
        return false;
    }

    /**
     * Gets Donkey's x-coordinate.
     *
     * @return Donkey's x-coordinate
     */
    public double getX() {
        return X;
    }

    /**
     * Gets Donkey's y-coordinate.
     *
     * @return Donkey's y-coordinate
     */
    public double getY() {
        return y;
    }
}