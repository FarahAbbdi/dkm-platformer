import bagel.*;
import java.util.Random;

/**
 * Represents an intelligent monkey enemy that can follow waypoints and throw bananas.
 */
public class IntelligentMonkey extends Monkey {
    // Sprite images
    private static final Image INTEL_MONKEY_RIGHT_IMAGE = new Image("res/intelli_monkey_right.png");
    private static final Image INTEL_MONKEY_LEFT_IMAGE = new Image("res/intelli_monkey_left.png");

    // Current sprite
    private Image currentImage;

    // Shooting properties
    private static final int SHOOT_INTERVAL = 300; // 5 seconds at 60 FPS
    private int shootTimer = 0;

    // Velocity for gravity
    private double velocityY = 0;

    /**
     * Creates a new intelligent monkey at the specified position.
     *
     * @param x The initial x-coordinate
     * @param y The initial y-coordinate
     * @param direction Initial facing direction ("left" or "right")
     * @param waypoints Array of waypoint distances for monkey movement
     */
    public IntelligentMonkey(double x, double y, String direction, int[] waypoints) {
        super(x, y, direction, waypoints);
        this.width = INTEL_MONKEY_RIGHT_IMAGE.getWidth();
        this.height = INTEL_MONKEY_RIGHT_IMAGE.getHeight();
        changeSprite();
    }

    /**
     * Updates the monkey's position using waypoints, applies gravity, and handles shooting.
     *
     * @param platforms The platforms to check for collisions
     */
    @Override
    public void update(Platform[] platforms) {
        if (isDestroyed) return;

        // Apply gravity
        velocityY += Physics.INTEL_MONKEY_GRAVITY;
        velocityY = Math.min(Physics.INTEL_MONKEY_TERMINAL_VELOCITY, velocityY);

        // Move vertically based on gravity
        y += velocityY;

        // Check platform collisions
        boolean onPlatform = false;
        Platform currentPlatform = null;

        for (Platform platform : platforms) {
            if (platform == null) continue;

            double monkeyBottom = y + height/2;
            double platformTop = platform.getY() - platform.getHeight()/2;

            // Check if monkey's feet are on the platform
            if (Math.abs(monkeyBottom - platformTop) < 5 &&
                    x + width/2 >= platform.getX() - platform.getWidth()/2 &&
                    x - width/2 <= platform.getX() + platform.getWidth()/2) {
                // Snap to platform
                y = platformTop - height/2;
                velocityY = 0;
                onPlatform = true;
                currentPlatform = platform;
                break;
            }
        }

        // Only move horizontally if on a platform
        if (onPlatform) {
            // Move horizontally
            x += velocity;

            // Update distance traveled
            distanceTravelled += Math.abs(velocity);

            // Check if reached current waypoint
            boolean reachedWaypoint = false;
            if (waypoints != null && waypoints.length > 0 &&
                    distanceTravelled >= waypoints[currentRouteIndex]) {
                reachedWaypoint = true;
            }

            // Check platform edges
            boolean atPlatformEdge = false;
            if (currentPlatform != null) {
                double platformLeft = currentPlatform.getX() - currentPlatform.getWidth()/2;
                double platformRight = currentPlatform.getX() + currentPlatform.getWidth()/2;

                // Check if the monkey would walk off the platform
                if ((isFacingRight && x + width/2 + velocity > platformRight) ||
                        (!isFacingRight && x - width/2 + velocity < platformLeft)) {
                    atPlatformEdge = true;
                }
            }

            // Check if should turn around
            if (reachedWaypoint || atPlatformEdge) {
                // Turn around
                isFacingRight = !isFacingRight;
                velocity = isFacingRight ? MONKEY_SPEED : -MONKEY_SPEED;
                changeSprite();

                // Reset distance and update waypoint index if reached waypoint
                if (reachedWaypoint && waypoints != null && waypoints.length > 0) {
                    distanceTravelled = 0;
                    currentRouteIndex = (currentRouteIndex + 1) % waypoints.length;
                }
            }
        }

        // We're not handling shooting here anymore
        // It's now handled by the checkThrowBanana method
    }

    /**
     * Checks if the monkey should throw a banana this frame.
     *
     * @return A new Banana object if thrown, null otherwise
     */
    public Banana checkThrowBanana() {
        // Increment timer
        shootTimer++;

        // Throw banana every 5 seconds (300 frames at 60 FPS)
        if (shootTimer >= SHOOT_INTERVAL) {
            shootTimer = 0;
            return createBanana();
        }

        return null;
    }

    /**
     * Creates a banana projectile.
     *
     * @return The created Banana object
     */
    private Banana createBanana() {
        // Calculate spawn position in front of the monkey
        double bananaX = isFacingRight ?
                x + width/2 : // At the right edge when facing right
                x - width/2;  // At the left edge when facing left

        // At the center height of the monkey
        double bananaY = y;

        // Create and return new banana
        return new Banana(bananaX, bananaY, isFacingRight);
    }

    /**
     * Draws the monkey on screen.
     */
    @Override
    public void draw() {
        if (!isDestroyed) {
            currentImage.draw(x, y);
        }
    }

    /**
     * Changes the monkey's sprite based on direction.
     */
    @Override
    public void changeSprite() {
        currentImage = isFacingRight ? INTEL_MONKEY_RIGHT_IMAGE : INTEL_MONKEY_LEFT_IMAGE;
    }
}